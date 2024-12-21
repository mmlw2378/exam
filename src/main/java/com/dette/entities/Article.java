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
@EqualsAndHashCode()
@ToString()
public class Article {
    private int id;
    private String nom;
    private int quantiteEnStock;
    private double prix;
    private int quantiteDemandee;
}
