package com.dette.repository.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dette.core.bd.DatabaseImpl;
import com.dette.entities.Role;
import com.dette.entities.Utilisateur;
import com.dette.repository.list.UtilisateurRepository;

public class UtilisateurRepositoryBd extends RepositoryBdImpl<Utilisateur> implements UtilisateurRepository {

    public UtilisateurRepositoryBd() {
        super(Utilisateur.class);
    }

    @Override
    public boolean loginExist(String login) {
        String query = "SELECT COUNT(*) FROM Utilisateur WHERE login = ?";
        int count = 0;
        return count > 0;
    }

    @Override
    public Utilisateur seConnecter(String login, String password) {
        String query = "SELECT * FROM \"Utilisateur\" WHERE \"login\" = ? AND \"password\" = ?";
        Utilisateur utilisateur = null;

        DatabaseImpl database = new DatabaseImpl();
        database.OpenConnection();

        try {
            database.initPreparedStatement(query);
            PreparedStatement stmt = database.statement;
            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setLogin(rs.getString("login"));
                utilisateur.setPassword(rs.getString("password"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));

                String roleStr = rs.getString("role"); 
                Role role = Role.valueOf(roleStr.toUpperCase());
                utilisateur.setRole(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.CloseConnection();
        }

        return utilisateur;
    }





    public void creerCompteAvecRole(Utilisateur utilisateur) {
        if (utilisateur.getRole() == null || 
            (utilisateur.getRole() != Role.BOUTIQUIER && utilisateur.getRole() != Role.ADMIN)) {
            throw new IllegalArgumentException("Le rôle de l'utilisateur doit être 'Boutiquier' ou 'Admin'.");
        }

        if (utilisateurExists(utilisateur.getLogin())) {
            System.out.println("Erreur : L'utilisateur avec le login '" + utilisateur.getLogin() + "' existe déjà.");
            return; 
        }

        String sql = "INSERT INTO \"Utilisateur\" (nom, prenom, login, password, role) VALUES (?, ?, ?, ?, ?)";
        DatabaseImpl database = new DatabaseImpl();
        database.OpenConnection();

        try {
            database.initPreparedStatement(sql);
            database.statement.setString(1, utilisateur.getNom());
            database.statement.setString(2, utilisateur.getPrenom());
            database.statement.setString(3, utilisateur.getLogin());
            database.statement.setString(4, utilisateur.getPassword());
            database.statement.setString(5, utilisateur.getRole().toString());
            int rows = database.statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Compte utilisateur avec le rôle " + utilisateur.getRole() + " créé avec succès !");
            } else {
                System.out.println("Échec de la création du compte utilisateur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.CloseConnection();
        }
    }



    private boolean utilisateurExists(String login) {
        String sql = "SELECT COUNT(*) FROM \"Utilisateur\" WHERE login = ?";
        DatabaseImpl database = new DatabaseImpl();
        database.OpenConnection();
    
        try {
            database.initPreparedStatement(sql);
            database.statement.setString(1, login);
            ResultSet rs = database.statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Retourne true si un utilisateur avec ce login existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.CloseConnection();
        }
        return false;
    }




    public void desactiverCompte(String login) {
        String sql = "UPDATE \"Utilisateur\" SET statut = 'désactivé' WHERE login = ?";
        DatabaseImpl database = new DatabaseImpl();
        database.OpenConnection();
    
        try {
            database.initPreparedStatement(sql);
            database.statement.setString(1, login);
            int rows = database.statement.executeUpdate();
    
            if (rows > 0) {
                System.out.println("Le compte avec le login '" + login + "' a été désactivé avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec ce login.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.CloseConnection();
        }
    }
    
    public void activerCompte(String login) {
        String sql = "UPDATE \"Utilisateur\" SET statut = 'actif' WHERE login = ?";
        DatabaseImpl database = new DatabaseImpl();
        database.OpenConnection();
    
        try {
            database.initPreparedStatement(sql);
            database.statement.setString(1, login);
            int rows = database.statement.executeUpdate();
    
            if (rows > 0) {
                System.out.println("Le compte avec le login '" + login + "' a été activé avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec ce login.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.CloseConnection();
        }
    }
    

    

}