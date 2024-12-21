package com.dette.repository.list;

import java.util.List;

import com.dette.core.repository.Repository;
import com.dette.entities.Paiement;

public interface PaiementRepository extends Repository<Paiement>{

    List<Paiement> listerPaiementsParDette(int detteId) ;

    void save(Paiement paiement);

}
