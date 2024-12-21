package com.dette.services;

import com.dette.entities.Paiement;

import java.util.List;


public interface PaiementService extends Service <Paiement> {
    List <Paiement> getPaiementsParDette(int detteId);
}
