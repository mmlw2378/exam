package com.dette.services.Impl;

import java.util.List;

import com.dette.entities.Paiement;
import com.dette.repository.list.PaiementRepository;
import com.dette.repository.bd.PaiementRepositoryBd;
import com.dette.services.PaiementService;

public class PaiementServiceImpl extends ServiceImpl<Paiement> implements PaiementService {

    private final PaiementRepository paiementRepository;
    
        public PaiementServiceImpl(PaiementRepositoryBd repository) {
            super(repository);
            this.paiementRepository = (PaiementRepository) repository;
    }


    public List<Paiement> getPaiementsParDette(int detteId) {
    return paiementRepository.listerPaiementsParDette(detteId);
}

}