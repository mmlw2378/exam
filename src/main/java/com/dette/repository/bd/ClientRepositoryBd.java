package com.dette.repository.bd;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dette.core.bd.DatabaseImpl;
import com.dette.entities.Client;
import com.dette.entities.Utilisateur;
import com.dette.repository.list.ClientRepository;

public class ClientRepositoryBd extends RepositoryBdImpl<Client> implements ClientRepository {

    public ClientRepositoryBd() {
        super(Client.class);
    }

    @Override
    public Client readClientBySurname(String surname) {
        String sql = "SELECT * FROM client WHERE surname = ?";
        Client client = null;
        try {
            initPreparedStatement(sql);
            statement.setString(1, surname);
            ResultSet rs = this.executeSelect();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.CloseConnection();
        }
        return client;
    }

    @Override
    public Client readClientByTelephone(String telephone) {
        String sql = "SELECT * FROM client WHERE telephone = ?";
        Client client = null;
        try {
            initPreparedStatement(sql);
            statement.setString(1, telephone);
            ResultSet rs = this.executeSelect();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.CloseConnection();
        }
        return client;
    }

    @Override
    public Client findById(int id) {
        Client client = null;
        String sql = "SELECT * FROM client WHERE id = ?";
        try {
            initPreparedStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }
        return client;
    }


    public void createClientWithAccount(Client client, Utilisateur utilisateur) {
        try {
            String utilisateurSql = "INSERT INTO \"utilisateur\" (nom, prenom, login, password, role) VALUES (?, ?, ?, ?,?)";
            initPreparedStatement(utilisateurSql);
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPrenom());
            statement.setString(3, utilisateur.getLogin());
            statement.setString(4, utilisateur.getPassword());
            statement.setString(5, utilisateur.getRole().toString()); 

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int utilisateurId = rs.getInt(1);
                utilisateur.setId(utilisateurId);

                String clientSql = "INSERT INTO client (surname, telephone, adresse, utilisateur_id) VALUES (?, ?, ?, ?)";
                initPreparedStatement(clientSql);
                statement.setString(1, client.getSurname());
                statement.setString(2, client.getTelephone());
                statement.setString(3, client.getAdresse());
                statement.setInt(4, utilisateurId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }
    }



    public void creerComptePourClient(int clientId, Utilisateur utilisateur) {
        String sqlCheckClient = "SELECT utilisateur_id FROM client WHERE id = ?";
        String sqlUpdateClient = "UPDATE client SET utilisateur_id = ? WHERE id = ?";
        
        if (utilisateur.getRole() == null) {
            throw new IllegalArgumentException("Le rôle de l'utilisateur doit être spécifié.");
        }
        
        DatabaseImpl database = new DatabaseImpl();
        database.OpenConnection();
        
        try {
            database.initPreparedStatement(sqlCheckClient);
            database.statement.setInt(1, clientId);
            ResultSet rs = database.executeSelect();
            
            if (rs.next() && rs.getInt("utilisateur_id") == 0) {
                String sqlInsertUtilisateur = "INSERT INTO \"utilisateur\" (nom, prenom, login, password, role) VALUES (?, ?, ?, ?, ?)";
                database.initPreparedStatement(sqlInsertUtilisateur);
                database.statement.setString(1, utilisateur.getNom());
                database.statement.setString(2, utilisateur.getPrenom());
                database.statement.setString(3, utilisateur.getLogin());
                database.statement.setString(4, utilisateur.getPassword());
                database.statement.setString(5, utilisateur.getRole().toString()); 
                database.statement.executeUpdate();
                
                ResultSet rsUtilisateur = database.statement.getGeneratedKeys();
                if (rsUtilisateur.next()) {
                    int utilisateurId = rsUtilisateur.getInt(1);
                    
                    database.initPreparedStatement(sqlUpdateClient);
                    database.statement.setInt(1, utilisateurId);
                    database.statement.setInt(2, clientId);
                    database.statement.executeUpdate();
                }
            } else {
                throw new IllegalArgumentException("Le client a déjà un compte utilisateur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.CloseConnection();
        }
    }

    public Client findByUser(Utilisateur utilisateur) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUser'");
    }


    public Client obtenirClientConnecte(int utilisateurId) {
        if (utilisateurId <= 0) {
            System.out.println("Aucun utilisateur connecté ou ID invalide.");
            return null;
        }

        Client client = null;
        String sql = "SELECT c.id, c.surname, c.telephone, c.adresse, c.cumulMontantDus, c.utilisateur_id " +
                     "FROM client c WHERE c.utilisateur_id = ?";

        try {
            initPreparedStatement(sql);
            statement.setInt(1, utilisateurId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));
                client.setCumulMontantDus(rs.getDouble("cumulMontantDus"));

                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("uutilisateur_id"));
                client.setUtilisateur(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }

        if (client == null) {
            System.out.println("Aucun client trouvé pour l'utilisateur connecté.");
        }

        return client;
    }

    

    
}