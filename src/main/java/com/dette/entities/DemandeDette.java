package com.dette.entities;

import java.sql.Date;
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
@EqualsAndHashCode
@ToString()
public class DemandeDette {
    private int id; 
    private Client client; 
    private String etat; 
    private List<Article> articles; 
    private Date date;
    private double montant;
    private String commentaire; 

}