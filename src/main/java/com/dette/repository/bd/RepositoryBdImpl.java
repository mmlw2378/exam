package com.dette.repository.bd;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.dette.core.bd.DatabaseImpl;
import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.entities.Utilisateur;

public abstract class RepositoryBdImpl<T> extends DatabaseImpl {
    private final Class<T> entityClass;

    public RepositoryBdImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public int create(T entity) {

        if (entity instanceof Utilisateur) {
            Utilisateur Utilisateur = (Utilisateur) entity;

            if (Utilisateur.getRole() == null) {
                throw new IllegalArgumentException("Le rôle de l'utilisateur doit être spécifié.");
            }
        }
        if (entity instanceof Client) {
            Client client = (Client) entity;

            if (isSurnameExists(client.getSurname())) {
                throw new IllegalArgumentException("Le surnom existe déjà dans la base de données.");
            }

            if (isTelephoneExists(client.getTelephone())) {
                throw new IllegalArgumentException("Le numéro de téléphone existe déjà dans la base de données.");
            }
        }

        String sql = buildInsertQuery(entity);
        int generatedId = -1;

        try {
            initPreparedStatement(sql);
            setInsertParameters(entity);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
                setGeneratedId(entity, generatedId);
            }

            if (entity instanceof Dette) {
                Dette dette = (Dette) entity;
                updateCumulMontantDus(dette.getClient().getId(), dette.getMontant());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }

        return generatedId;
    }

    public void updateCumulMontantDus(int clientId, double montant) {
        String sql = "SELECT SUM(montantrestant) FROM dette WHERE client_id = ? AND montantrestant > 0";
        
        try {
            initPreparedStatement(sql);
            statement.setInt(1, clientId);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                double cumulMontantDus = rs.getDouble(1);
                String updateSql = "UPDATE client SET cumulMontantDus = ? WHERE id = ?";
                initPreparedStatement(updateSql);
                statement.setDouble(1, cumulMontantDus);
                statement.setInt(2, clientId);
                statement.executeUpdate();
                System.out.println("CumulMontantDus mis à jour avec succès.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }
    }

    public void createPaiement(Dette dette, double paiement) {
        String sql = "UPDATE dette SET montantverse = montantverse + ?, montantrestant = montantrestant - ? WHERE id = ?";
        
        try {
            initPreparedStatement(sql);
            statement.setDouble(1, paiement);
            statement.setDouble(2, paiement);
            statement.setInt(3, dette.getId());
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Paiement enregistré avec succès.");
                updateCumulMontantDus(dette.getClient().getId(), 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }
    }
    
    

    private boolean isSurnameExists(String surname) {
        String sql = "SELECT COUNT(*) FROM client WHERE surname = ?";
        try {
            initPreparedStatement(sql);
            statement.setString(1, surname);
            ResultSet rs = this.executeSelect();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.CloseConnection();
        }
        return false;
    }

    private boolean isTelephoneExists(String telephone) {
        String sql = "SELECT COUNT(*) FROM client WHERE telephone = ?";
        try {
            initPreparedStatement(sql);
            statement.setString(1, telephone);
            ResultSet rs = this.executeSelect();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.CloseConnection();
        }
        return false;
    }

    public List<T> read(Boolean hasAccount) {
        String sql = buildSelectQuery(hasAccount);
        List<T> entities = new ArrayList<>();

        try {
            initPreparedStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }

        return entities;
    }

    private String buildInsertQuery(T entity) {
        if (entity instanceof Utilisateur) {
            return "INSERT INTO \"Utilisateur\"(nom, prenom, login, password) VALUES (?, ?, ?, ?)";
        } else if (entity instanceof Client) {
            return "INSERT INTO \"client\"(surname, telephone, adresse, Utilisateur_id) VALUES (?, ?, ?, ?)";
        } else if (entity instanceof Dette) {
            return "INSERT INTO \"dette\"(client_id, date, montant, montantverse, montantrestant) VALUES (?, ?, ?, ?, ?)";
        } else {
            throw new UnsupportedOperationException("Unsupported entity type: " + entity.getClass().getName());
        }
    }

    private String buildSelectQuery(Boolean hasAccount) {
        if (entityClass.equals(Client.class)) {
            if (hasAccount == null) {
                return "SELECT c.id, c.surname, c.telephone, c.adresse, " +
                       "(SELECT COALESCE(SUM(d.montantrestant), 0) FROM dette d WHERE d.client_id = c.id AND d.montantrestant > 0) AS cumulMontantDus, " +
                       "c.Utilisateur_id, u.nom, u.prenom, u.login, u.password " +
                       "FROM client c LEFT JOIN \"Utilisateur\" u ON c.Utilisateur_id = u.id";
            }
            return hasAccount
                    ? "SELECT c.id, c.surname, c.telephone, c.adresse, " +
                      "(SELECT COALESCE(SUM(d.montantrestant), 0) FROM dette d WHERE d.client_id = c.id AND d.montantrestant > 0) AS cumulMontantDus, " +
                      "c.Utilisateur_id, u.nom, u.prenom, u.login, u.password " +
                      "FROM client c LEFT JOIN \"Utilisateur\" u ON c.Utilisateur_id = u.id WHERE c.Utilisateur_id IS NOT NULL"
                    : "SELECT c.id, c.surname, c.telephone, c.adresse, " +
                      "(SELECT COALESCE(SUM(d.montantrestant), 0) FROM dette d WHERE d.client_id = c.id AND d.montantrestant > 0) AS cumulMontantDus, " +
                      "c.Utilisateur_id " +
                      "FROM client c WHERE c.Utilisateur_id IS NULL";
        } else if (entityClass.equals(Utilisateur.class)) {
            return "SELECT id, nom, prenom, login, password FROM \"Utilisateur\"";
        } else {
            throw new UnsupportedOperationException("Unsupported entity type");
        }
    }
    

    private void setInsertParameters(T entity) throws SQLException {
        if (entity instanceof Utilisateur) {
            Utilisateur Utilisateur = (Utilisateur) entity;
            statement.setString(1, Utilisateur.getNom());
            statement.setString(2, Utilisateur.getPrenom());
            statement.setString(3, Utilisateur.getLogin());
            statement.setString(4, Utilisateur.getPassword());
            statement.setString(5, Utilisateur.getRole().toString()); 
        } else if (entity instanceof Client) {
            Client client = (Client) entity;
            statement.setString(1, client.getSurname());
            statement.setString(2, client.getTelephone());
            statement.setString(3, client.getAdresse());
            if (client.getUtilisateur() != null) {
                statement.setInt(4, client.getUtilisateur().getId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
        } else if (entity instanceof Dette) {
            Dette dette = (Dette) entity;
            statement.setInt(1, dette.getClient().getId());
            statement.setDate(2, (Date) dette.getDate());
            statement.setDouble(3, dette.getMontant());
            statement.setDouble(4, dette.getMontantVerse());
            statement.setDouble(5, dette.getMontantRestant());
        }
    }

    private void setGeneratedId(T entity, int id) {
        if (entity instanceof Utilisateur) {
            ((Utilisateur) entity).setId(id);
        } else if (entity instanceof Client) {
            ((Client) entity).setId(id);
        }
    }

    private T mapResultSetToEntity(ResultSet rs) throws SQLException {
        if (entityClass.equals(Client.class)) {
            Client client = new Client();
            client.setId(rs.getInt("id"));
            client.setSurname(rs.getString("surname"));
            client.setTelephone(rs.getString("telephone"));
            client.setAdresse(rs.getString("adresse"));
            client.setCumulMontantDus(rs.getDouble("cumulMontantDus"));  
    
            int utilisateurId = rs.getInt("Utilisateur_id");
            if (utilisateurId > 0) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(utilisateurId);
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setLogin(rs.getString("login"));
                utilisateur.setPassword(rs.getString("password"));
                client.setUtilisateur(utilisateur);
            }
    
            return (T) client;
        } else if (entityClass.equals(Utilisateur.class)) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setLogin(rs.getString("login"));
            utilisateur.setPassword(rs.getString("password"));
            return (T) utilisateur;
        } else {
            throw new UnsupportedOperationException("Unsupported entity type");
        }
    }
    
}