package com.dette.repository.bd;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dette.core.bd.DatabaseImpl;
import com.dette.entities.Paiement;
import com.dette.repository.list.PaiementRepository;

public class PaiementRepositoryBd extends RepositoryBdImpl<Paiement> implements PaiementRepository{
    
     public PaiementRepositoryBd() {
        super(Paiement.class);
    }


    public List<Paiement> listerPaiementsParDette(int detteId) {
    String sql = "SELECT * FROM paiement WHERE dette_id = ?";
    List<Paiement> paiements = new ArrayList<>();
    DatabaseImpl database = new DatabaseImpl();
    database.OpenConnection();

    try {
        database.initPreparedStatement(sql);
        database.statement.setInt(1, detteId);
        ResultSet rs = database.statement.executeQuery();
        while (rs.next()) {
            Paiement paiement = new Paiement();
            paiement.setId(rs.getInt("id"));
            paiement.setMontant(rs.getDouble("montant"));
            paiement.setDate(rs.getDate("date"));
            paiement.setDette_id(detteId);
            paiements.add(paiement);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        database.CloseConnection();
    }

    return paiements;
}



public void enregistrerPaiement(Paiement paiement, int detteId, double montantRestant) {
    DatabaseImpl database = new DatabaseImpl();
    database.OpenConnection();

    String sqlPaiement = "INSERT INTO \"paiement\" (dette_id, montant, date) VALUES (?, ?, ?)";
    try {
        database.initPreparedStatement(sqlPaiement);
        database.statement.setInt(1, detteId);
        database.statement.setDouble(2, paiement.getMontant());
        database.statement.setDate(3, (Date) paiement.getDate());
        database.statement.executeUpdate();

        String sqlDette;
        if (paiement.getMontant() >= montantRestant) {
            sqlDette = "UPDATE dette SET montantrestant = 0, solde = true WHERE id = ?";
        } else {
            sqlDette = "UPDATE dette SET montantrestant = montantrestant - ? WHERE id = ?";
        }

        database.initPreparedStatement(sqlDette);
        database.statement.setDouble(1, paiement.getMontant());
        database.statement.setInt(2, detteId);
        database.statement.executeUpdate();

        System.out.println("Paiement enregistré avec succès et montant restant mis à jour.");
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        database.CloseConnection();
    }
}


@Override
public void save(Paiement paiement) {
    String sql = "INSERT INTO paiement (montant, date, dette_id) VALUES (?, ?, ?)";

    try {
        initPreparedStatement(sql);
        statement.setDouble(1, paiement.getMontant());
        statement.setDate(2, new java.sql.Date(paiement.getDate().getTime()));  
        statement.setInt(3, paiement.getDette_id());
        statement.executeUpdate();
        System.out.println("Paiement enregistré avec succès !");
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        this.CloseConnection();
    }
}





}