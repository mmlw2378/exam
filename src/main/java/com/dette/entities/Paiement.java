package com.dette.entities;

import java.util.Date;

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

public class Paiement {
    private int id;
    private Date date;
    private double montant;
    private int dette_id;
    private Dette dette;


    public Paiement(Date date, double montant) {
        this.date = date;
        this.montant = montant;
    }
}