package com.dette.repository.bd;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.DemandeDette;
import com.dette.entities.Dette;
import com.dette.repository.list.DemandeDetteRepository;

public class DemandeDetteRepositoryBd extends RepositoryBdImpl<DemandeDette> implements DemandeDetteRepository {
    public DemandeDetteRepositoryBd() {
        super(DemandeDette.class);
    }


    public List<DemandeDette> getDemandesDetteByEtat(String etat) {
    String sql = "SELECT dd.id, dd.date, dd.etat, dd.client_id, c.surname, c.telephone " +
                 "FROM \"demandeDette\" dd " +
                 "INNER JOIN client c ON dd.client_id = c.id " +
                 "WHERE dd.etat = ?";
    List<DemandeDette> demandes = new ArrayList<>();

    try {
        initPreparedStatement(sql);
        statement.setString(1, etat);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            DemandeDette demande = new DemandeDette();
            demande.setId(rs.getInt("id"));
            demande.setDate(rs.getDate("date"));
            demande.setEtat(rs.getString("etat"));

            Client client = new Client();
            client.setId(rs.getInt("client_id"));
            client.setSurname(rs.getString("surname"));
            client.setTelephone(rs.getString("telephone"));
            demande.setClient(client);

            demandes.add(demande);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        CloseConnection();
    }

    return demandes;
}



public void updateDemandeEtat(int demandeId, String nouvelEtat) {
    String sql = "UPDATE \"demandeDette\" SET etat = ? WHERE id = ?";
    try {
        initPreparedStatement(sql);
        statement.setString(1, nouvelEtat);
        statement.setInt(2, demandeId);
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        CloseConnection();
    }
}




public void save(DemandeDette demande) {
    String sql = "INSERT INTO \"demandeDette\" (client_id, montant, etat, date) VALUES (?, ?, ?, ?)";

    try {
        initPreparedStatement(sql);
        statement.setInt(1, demande.getClient().getId());
        statement.setDouble(2, demande.getMontant());
        statement.setString(3, demande.getEtat());
        statement.setDate(4, new java.sql.Date(demande.getDate().getTime())); 
        statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            demande.setId(rs.getInt(1));
        }

        if (demande.getArticles() != null) {
            for (Article article : demande.getArticles()) {
                String sqlAssocierArticle = "INSERT INTO \"demandeDetteArticle\" (demande_dette_id, article_id) VALUES (?, ?)";
                initPreparedStatement(sqlAssocierArticle);
                statement.setInt(1, demande.getId());
                statement.setInt(2, article.getId());
                statement.executeUpdate();
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        CloseConnection();
    }
}



public int create(DemandeDette demande) {
    String sql = "INSERT INTO \"demandeDette\" (client_id, montant, etat, date) VALUES (?, ?, ?, ?)";
    
    try {
        initPreparedStatement(sql);
        statement.setInt(1, demande.getClient().getId());
        statement.setDouble(2, demande.getMontant());
        statement.setString(3, demande.getEtat());
        statement.setDate(4, new java.sql.Date(demande.getDate().getTime())); 
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        CloseConnection();
    }
        return 0;
}


public List<DemandeDette> findByClient(Client client) {
    String sql = "SELECT dd.id, dd.date, dd.etat, dd.client_id, c.surname, c.telephone " +
                 "FROM \"demandeDette\" dd " +
                 "INNER JOIN client c ON dd.client_id = c.id " +
                 "WHERE dd.client_id = ?";
    List<DemandeDette> demandes = new ArrayList<>();

    try {
        initPreparedStatement(sql);
        statement.setInt(1, client.getId());
        ResultSet rs = statement.executeQuery();

        if (!rs.next()) {
            System.out.println("Aucune demande de dette trouvée pour ce client.");
            return demandes;  
        }

        do {
            DemandeDette demande = new DemandeDette();
            demande.setId(rs.getInt("id"));
            demande.setDate(rs.getDate("date"));
            demande.setEtat(rs.getString("etat"));

            Client mappedClient = new Client();
            mappedClient.setId(rs.getInt("client_id"));
            mappedClient.setSurname(rs.getString("surname"));
            mappedClient.setTelephone(rs.getString("telephone"));
            demande.setClient(mappedClient);

            demandes.add(demande);
        } while (rs.next());
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        CloseConnection();
    }

    return demandes;
}





public void relancerDemandeDetteAnnulee(int demandeId, String commentaire) {
    String sqlSelect = "SELECT status FROM \"demandeDette\"WHERE id = ?";
    String sqlUpdate = "UPDATE \"demandeDette\" SET status = ?, commentaire = ? WHERE id = ?";
    
    try {
        initPreparedStatement(sqlSelect);
        statement.setInt(1, demandeId);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            String status = rs.getString("status");
            if ("ANNULEE".equalsIgnoreCase(status)) {
                initPreparedStatement(sqlUpdate);
                statement.setString(1, "RELANCE");
                statement.setString(2, commentaire);
                statement.setInt(3, demandeId);
                statement.executeUpdate();
                System.out.println("Demande de dette annulée relancée avec succès.");
            } else {
                System.out.println("La demande n'est pas annulée. Statut actuel : " + status);
            }
        } else {
            System.out.println("Aucune demande de dette trouvée avec l'ID spécifié.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        CloseConnection();
    }
}


public DemandeDette findById(int demandeId) {
    String sql = "SELECT * FROM \"demandeDette\" WHERE id = ?";
    DemandeDette demande = null;

    try {
        initPreparedStatement(sql);
        statement.setInt(1, demandeId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            demande = new DemandeDette();
            demande.setId(resultSet.getInt("id"));
            demande.setEtat(resultSet.getString("etat"));
            demande.setCommentaire(resultSet.getString("commentaire"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        CloseConnection();
    }

    return demande;
}



public void updateDemandeEtatAvecCommentaire(int demandeId, String etat, String commentaire) {
    String sql = "UPDATE \"demandeDette\" SET etat = ?, commentaire = ? WHERE id = ?";
    try {
        initPreparedStatement(sql);
        statement.setString(1, etat);
        statement.setString(2, commentaire);
        statement.setInt(3, demandeId);
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        CloseConnection();
    }
}


public List<Dette> readDemandesDetteParEtat(int clientId, String etat) {
        List<Dette> demandes = new ArrayList<>();
        String sql = "SELECT * FROM \"demandeDette\" WHERE client_id = ? AND etat = ?";

        try {
            initPreparedStatement(sql);
            statement.setInt(1, clientId);
            statement.setString(2, etat);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Dette dette = new Dette();
                dette.setId(rs.getInt("id"));
                dette.setDate(rs.getDate("date"));
                dette.setMontant(rs.getDouble("montant"));
                // dette.setMontantVerse(rs.getDouble("montantverse"));
                // dette.setMontantRestant(rs.getDouble("montantrestant"));
                dette.setEtat(rs.getString("etat"));
                demandes.add(dette);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }

        return demandes;
    }



}