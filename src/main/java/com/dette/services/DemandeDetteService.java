package com.dette.services;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.DemandeDette;
import java.util.List;

public interface DemandeDetteService extends Service<DemandeDette> {
    List<DemandeDette> listerDemandes(String etat);
    List<Article> voirArticles(int demandeId);
    void validerDemande(int demandeId);
    void refuserDemande(int demandeId);
    void creer(DemandeDette demande);
    List<DemandeDette> filtrerDemandesParEtat(Client client, String etat);
    void relancerDemandeAnnulee(int demandeId, String commentaire);
}