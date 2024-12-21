package com.dette.entities;

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
@EqualsAndHashCode(of = { "surname", "telephone" })
@ToString()
public class Client {
    private int id;
    private String surname;
    private String telephone;
    private String adresse;
    private double cumulMontantDus;

    private Utilisateur utilisateur;

    

}