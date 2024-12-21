

package com.dette.views;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.DemandeDette;
import com.dette.entities.Paiement;
import com.dette.repository.bd.PaiementRepositoryBd;
import com.dette.services.DemandeDetteService;

public class DemandeDetteView {
    private final Scanner scanner;
    private final DemandeDetteService service;

    public DemandeDetteView(Scanner scanner, DemandeDetteService service) {
        this.scanner = scanner;
        this.service = service;

    }

    public void afficherDemandesFiltrees() {
        System.out.println("Choisissez un état (En Cours / Annulée): ");
        String etat = scanner.nextLine();

        List<DemandeDette> demandes = service.listerDemandes(etat);
        System.out.println("Demandes de dette (" + etat + "):");
        for (DemandeDette demande : demandes) {
            System.out.println("ID: " + demande.getId() + ", Client: " + demande.getClient().getSurname() +
                    ", Téléphone: " + demande.getClient().getTelephone() + ", Date: " + demande.getDate());
        }
    }

    public void afficherArticlesPourDemande() {
        System.out.println("Entrez l'ID d'une demande pour voir les articles : ");
        int demandeId = scanner.nextInt();
        scanner.nextLine(); 

        List<Article> articles = service.voirArticles(demandeId);
        System.out.println("Articles pour la demande " + demandeId + ":");
        for (Article article : articles) {
            System.out.println("Nom: " + article.getNom() + ", Quantité demandée: " + article.getQuantiteDemandee() +
                    ", Stock disponible: " + article.getQuantiteEnStock());
        }
    }

    public void traiterDemande() {
        System.out.println("Entrez l'ID d'une demande à traiter : ");
        int demandeId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.println("Action (Valider / Refuser) : ");
        String action = scanner.nextLine();

        if (action.equalsIgnoreCase("Valider")) {
            service.validerDemande(demandeId);
            System.out.println("Demande validée !");
        } else if (action.equalsIgnoreCase("Refuser")) {
            service.refuserDemande(demandeId);
            System.out.println("Demande refusée !");
        } else {
            System.out.println("Action non reconnue.");
        }
    }



    public void creerDemandeDette() {
    System.out.println("Création d'une nouvelle demande de dette");
    
    System.out.println("Entrez l'ID du client : ");
    int clientId = scanner.nextInt();
    scanner.nextLine(); 

    System.out.println("Entrez le nombre d'articles : ");
    int nombreArticles = scanner.nextInt();
    scanner.nextLine(); 

    List<Article> articles = new ArrayList<>();
    for (int i = 0; i < nombreArticles; i++) {
        System.out.println("Entrez l'ID de l'article #" + (i + 1) + ": ");
        int articleId = scanner.nextInt();
        scanner.nextLine(); 
        Article article = new Article();
        article.setId(articleId);
        articles.add(article);
    }

    System.out.println("Entrez le montant de la demande : ");
    double montant = scanner.nextDouble();
    scanner.nextLine(); 

    System.out.println("Entrez l'état de la demande (En Cours/Valide) : ");
    String etat = scanner.nextLine();

    DemandeDette demande = new DemandeDette();
    Client client = new Client();
    client.setId(clientId);
    demande.setClient(client);
    demande.setArticles(articles);
    demande.setMontant(montant);
    demande.setEtat(etat);

    java.util.Date utilDate = new java.util.Date();  
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  
    
    demande.setDate(sqlDate);
    service.creer(demande); 
    System.out.println("Demande de dette créée avec succès !");
}



}