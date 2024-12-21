package com.dette.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@ToString()

public class Dette {
    private int id;
    private Client client;
    private Date date;
    private double montant;
    private double montantVerse;
    private double montantRestant;
    private String etat;
    
    private List<Article> articles = new ArrayList<>();
    private List<Paiement> paiements = new ArrayList<>();

    public void addPaiement(Paiement paiement) {
        this.paiements.add(paiement);
        this.montantVerse += paiement.getMontant(); 
        this.montantRestant = this.montant - this.montantVerse; 
    }
    

    



}