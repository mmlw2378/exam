package com.dette.repository.list;

import java.util.List;

import com.dette.core.repository.Repository;
import com.dette.entities.Article;
import com.dette.entities.Dette;
import com.dette.entities.Paiement;

public interface DetteRepository extends Repository<Dette> {
    Dette findById(int id);
    List<Dette> findUnpaidDebtsByClientId(int clientId);
    List<Article> findArticlesByDetteId(int detteId); 
    List<Paiement> findPaiementsByDetteId(int detteId);
    void update(Dette selectedDette);
    void savePaiement(Paiement paiement);
    void save(Dette dette);
}