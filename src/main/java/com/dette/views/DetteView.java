package com.dette.views;

import com.dette.entities.Dette;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Paiement;
import com.dette.repository.list.ArticleRepository;
import com.dette.repository.list.DetteRepository;
import com.dette.repository.list.PaiementRepository;
import com.dette.repository.bd.ClientRepositoryBd;


import java.sql.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class DetteView {
    private Scanner scanner;

    private DetteRepository detteRepository;
    private PaiementRepository paiementRepository;
    private ArticleRepository articleRepository;

    public DetteView(Scanner scanner, DetteRepository detteRepository) {
        this.scanner = scanner;
        this.detteRepository = detteRepository;
        this.paiementRepository = paiementRepository;
        this.articleRepository = articleRepository;
    }

    public Dette saisieDette() {
        Dette dette = new Dette();
    
        System.out.print("Entrez l'ID du client : ");
        int clientId = scanner.nextInt();
        scanner.nextLine();
    
        Client client = getClientById(clientId);
    
        if (client == null) {
            System.out.println("Impossible de créer une dette sans client valide.");
            return null;
        }
    
        System.out.print("Entrez la date de la dette (YYYY-MM-DD) : ");
        String dateString = scanner.nextLine();
        dette.setDate(Date.valueOf(dateString));
    
        System.out.print("Entrez le montant total : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();
    
        System.out.print("Entrez le montant versé : ");
        double montantVerse = scanner.nextDouble();
        scanner.nextLine();
    
        dette.setMontant(montant);
        dette.setMontantVerse(montantVerse);
        dette.setMontantRestant(montant - montantVerse);
    
        System.out.println("Montant restant : " + dette.getMontantRestant());
    
        try {
            detteRepository.save(dette);
            System.out.println("Dette créée avec succès pour le client " + client.getSurname());
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de la dette : " + e.getMessage());
        }
    
        // Ajout d'articles si nécessaire
        System.out.println("Voulez-vous ajouter des articles à cette dette ? (O/N)");
        char ajoutArticles = scanner.next().charAt(0);
        scanner.nextLine();
    
        if (Character.toUpperCase(ajoutArticles) == 'O') {
            System.out.print("Entrez le nombre d'articles à ajouter : ");
            int nombreArticles = scanner.nextInt();
            scanner.nextLine();
            ArrayList<Article> articles = new ArrayList<>();
    
            for (int i = 0; i < nombreArticles; i++) {
                System.out.print("Entrez le nom de l'article " + (i + 1) + " : ");
                String articleNom = scanner.nextLine();
                Article article = new Article();
                article.setNom(articleNom);
                articleRepository.save(article);
                articles.add(article);
            }
            dette.setArticles(articles);
        }
    
        // Ajout d'un paiement si nécessaire
        System.out.println("Voulez-vous ajouter un paiement à cette dette ? (O/N)");
        char ajoutPaiement = scanner.next().charAt(0);
        scanner.nextLine();
    
        if (Character.toUpperCase(ajoutPaiement) == 'O') {
            System.out.print("Entrez le montant du paiement : ");
            double paiementMontant = scanner.nextDouble();
            scanner.nextLine();
    
            Paiement paiement = new Paiement();
            paiement.setMontant(paiementMontant);
            paiement.setDette(dette);
            paiement.setDate(new java.util.Date());
    
            paiementRepository.save(paiement);
    
            dette.addPaiement(paiement);
            detteRepository.update(dette);
            System.out.println("Paiement enregistré avec succès !");
        }
    
        return dette;
    }

    private Client getClientById(int clientId) {
        ClientRepositoryBd clientRepositoryBd = new ClientRepositoryBd();  
        Client client = clientRepositoryBd.findById(clientId); 
        if (client == null) {
            System.out.println("Client non trouvé avec l'ID : " + clientId);
        }
        return client;
    }
    
    

    public void afficherDettesNonSoldees(List<Dette> dettes) {
        if (dettes.isEmpty()) {
            System.out.println("Aucune dette non soldée trouvée.");
            return;
        }

        System.out.println("Liste des dettes non soldées :");
        for (Dette dette : dettes) {
            System.out.println("ID : " + dette.getId());
            System.out.println("Date : " + dette.getDate());
            System.out.println("Montant : " + dette.getMontant());
            System.out.println("Montant versé : " + dette.getMontantVerse());
            System.out.println("Montant restant : " + dette.getMontantRestant());
            System.out.println("-----------------------------------");
        }

        System.out.print("Voulez-vous voir les détails d'une dette spécifique ? (O/N) : ");
        char choix = scanner.next().charAt(0);
        scanner.nextLine();

        if (Character.toUpperCase(choix) == 'O') {
            System.out.print("Entrez l'ID de la dette : ");
            int detteId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Voir les (A)rticles ou les (P)aiements ? : ");
            char option = scanner.next().charAt(0);
            scanner.nextLine();

            if (Character.toUpperCase(option) == 'A') {
                List<Article> articles = detteRepository.findArticlesByDetteId(detteId);
                afficherArticles(articles);
            } else if (Character.toUpperCase(option) == 'P') {
                List<Paiement> paiements = detteRepository.findPaiementsByDetteId(detteId);
                afficherPaiements(paiements);
            } else {
                System.out.println("Option invalide.");
            }
        }
    }

    private void afficherArticles(List<Article> articles) {
        if (articles.isEmpty()) {
            System.out.println("Aucun article trouvé pour cette dette.");
            return;
        }

        System.out.println("Articles liés à la dette :");
        for (Article article : articles) {
            System.out.println("ID : " + article.getId());
            System.out.println("Nom : " + article.getNom());
        }
    }

    private void afficherPaiements(List<Paiement> paiements) {
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement trouvé pour cette dette.");
            return;
        }

        System.out.println("Paiements liés à la dette :");
        for (Paiement paiement : paiements) {
            System.out.println("ID : " + paiement.getId());
            System.out.println("Montant : " + paiement.getMontant());
        }
    }

    public void afficherDettes(List<Dette> dettes) {
        for (Dette dette : dettes) {
            System.out.println(dette);
        }
    }

}
