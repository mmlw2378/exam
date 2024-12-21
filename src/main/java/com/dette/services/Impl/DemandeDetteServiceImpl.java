package com.dette.services.Impl;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.DemandeDette;
import com.dette.repository.bd.DemandeDetteRepositoryBd;
import com.dette.repository.bd.ArticleRepositoryBd;
import com.dette.services.DemandeDetteService;

import java.util.ArrayList;
import java.util.List;

public class DemandeDetteServiceImpl extends ServiceImpl<DemandeDette> implements DemandeDetteService {

    private final DemandeDetteRepositoryBd demandeRepository;
    private final ArticleRepositoryBd articleRepository;

    public DemandeDetteServiceImpl(DemandeDetteRepositoryBd demandeRepository, ArticleRepositoryBd articleRepository) {
        super(demandeRepository);
        this.demandeRepository = demandeRepository;
        this.articleRepository = articleRepository;

    }

    @Override
    public List<DemandeDette> listerDemandes(String etat) {
        return demandeRepository.getDemandesDetteByEtat(etat);
    }

    @Override
    public List<Article> voirArticles(int demandeId) {
        return articleRepository.findByDemandeId(demandeId);
    }

    @Override
    public void validerDemande(int demandeId) {
        demandeRepository.updateDemandeEtat(demandeId, "Validée");
    }

    @Override
    public void refuserDemande(int demandeId) {
        demandeRepository.updateDemandeEtat(demandeId, "Annulée");
    }

    @Override
    public void creer(DemandeDette demande) {
        demandeRepository.create(demande);
    }

    public List<DemandeDette> filtrerDemandesParEtat(Client client, String etat) {
        List<DemandeDette> demandesFiltrees = new ArrayList<>();

        if (client == null) {
            System.out.println("Le client est null, impossible de filtrer les demandes de dette.");
            return demandesFiltrees;
        }

        List<DemandeDette> demandesClient = demandeRepository.findByClient(client);

        for (DemandeDette demande : demandesClient) {
            if (demande.getEtat().equals(etat)) {
                demandesFiltrees.add(demande);
            }
        }

        return demandesFiltrees;
    }

    @Override
    public void relancerDemandeAnnulee(int demandeId, String commentaire) {
        DemandeDette demande = demandeRepository.findById(demandeId);

        if (demande == null) {
            System.out.println("La demande n'existe pas.");
            return;
        }

        if (!"Annulee".equalsIgnoreCase(demande.getEtat())) {
            System.out.println("Seules les demandes annulées peuvent être relancées.");
            return;
        }

        demande.setEtat("Relancee");
        demandeRepository.updateDemandeEtatAvecCommentaire(demandeId, "Relancee", commentaire);
        System.out.println("La demande a été relancée avec succès.");
    }

}