package com.dette;

import java.util.List;
import java.util.Scanner;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Utilisateur;
import com.dette.entities.Dette;
import com.dette.entities.Paiement;
import com.dette.entities.Role;
import com.dette.repository.bd.DetteRepositoryBd;
import com.dette.repository.bd.PaiementRepositoryBd;
import com.dette.repository.bd.ArticleRepositoryBd;
import com.dette.repository.bd.ClientRepositoryBd;
import com.dette.repository.bd.DemandeDetteRepositoryBd;
import com.dette.repository.bd.UtilisateurRepositoryBd;
import com.dette.services.ClientService;
import com.dette.services.DemandeDetteService;
import com.dette.services.UtilisateurService;
import com.dette.services.DetteService;
import com.dette.services.PaiementService;
import com.dette.services.Impl.ClientServiceImpl;
import com.dette.services.Impl.DemandeDetteServiceImpl;
import com.dette.services.Impl.UtilisateurServiceImpl;
import com.dette.services.Impl.DetteServiceImpl;
import com.dette.services.Impl.PaiementServiceImpl;
import com.dette.views.ArticleView;
import com.dette.views.ClientView;
import com.dette.views.DemandeDetteView;
import com.dette.views.UtilisateurView;
import com.dette.views.DetteView;
import com.dette.views.PaiementView;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClientView clientView = new ClientView(scanner);
        UtilisateurView utilisateurView = new UtilisateurView(scanner);
        ArticleView articleView = new ArticleView(scanner);
        PaiementView paiementView = new PaiementView(scanner);
        Client client;
        Utilisateur utilisateur;

        ClientRepositoryBd clientRepository = new ClientRepositoryBd();
        UtilisateurRepositoryBd utilisateurRepository = new UtilisateurRepositoryBd();
        DetteRepositoryBd detteRepository = new DetteRepositoryBd();
        ArticleRepositoryBd articleRepository = new ArticleRepositoryBd();
        DemandeDetteRepositoryBd demandeRepository = new DemandeDetteRepositoryBd();
        PaiementRepositoryBd paiementRepositoty = new PaiementRepositoryBd();

        DetteView detteView = new DetteView(scanner, detteRepository);

        DetteService detteService = new DetteServiceImpl(detteRepository);
        ClientService clientService = new ClientServiceImpl(clientRepository);
        UtilisateurService utilisateurService = new UtilisateurServiceImpl(utilisateurRepository);
        DemandeDetteService demandeDetteService = new DemandeDetteServiceImpl(demandeRepository, articleRepository);
        DemandeDetteView demandeDetteView = new DemandeDetteView(scanner, demandeDetteService);
        PaiementService paiementService = new PaiementServiceImpl(paiementRepositoty);

        int choix;
        Character response;

        System.out.println("-----------Connexion------------");

        System.out.print("Login : ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        utilisateur = utilisateurRepository.seConnecter(login, password);
        if (utilisateur == null) {
            System.out.println("Identifiants invalides !");
            return;
        }
        System.out.println("-------------Bienvenue--------------- " + utilisateur.getPrenom() + " " + utilisateur.getNom());

        if (utilisateur.getRole() == Role.BOUTIQUIER) {

            do {
                System.out.println("1 - Créer un nouveau client");
                System.out.println("2 - Lister les clients");
                System.out.println("3 - Filtrer les clients (avec ou sans compte)");
                System.out.println("4 - Rechercher un client (par surname ou téléphone)");
                System.out.println("5 - Créer une dette pour un client");
                System.out.println("6 - Lister les dettes non soldées d'un client");
                System.out.println("7 - Enregistrer un paiement pour une dette");
                System.out.println("8 - Lister les demandes de dette avec filtre");
                System.out.println("9 - Quitter");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();
                scanner.nextLine();
                switch (choix) {
                    case 1:
                        client = clientView.saisieClient();
                        if (clientService.surnameExist(client.getSurname()) == null) {
                            System.out.println("Voulez-vous ajouter un compte utilisateur ? (O/N)");
                            response = scanner.next().charAt(0);
                            if (Character.toUpperCase(response) == 'O') {
                                scanner.nextLine();
                                utilisateur = utilisateurView.saisieUtilisateur();
                                clientService.createClientWithAccount(client, utilisateur);
                                System.out.println("Client et compte utilisateur créés avec succès.");
                            } else {
                                clientService.create(client);
                                System.out.println("Client créé avec succès.");
                            }
                        } else {
                            System.out.println("Le client existe déjà : " + client.getSurname());
                        }
                        break;

                    case 2:
                        clientView.afficheClient(clientService.get(null));
                        break;

                    case 3:
                        System.out.println("Filtrer les clients :");
                        System.out.println("1 - Clients avec compte");
                        System.out.println("2 - Clients sans compte");
                        int filterChoice = scanner.nextInt();
                        boolean hasAccount = filterChoice == 1 ? Boolean.TRUE : Boolean.FALSE;
                        clientView.afficheClient(clientService.get(hasAccount));
                        break;
                    case 4:
                        System.out.println("Rechercher un client :");
                        System.out.println("1 - Par surname");
                        System.out.println("2 - Par téléphone");
                        System.out.print("Votre choix : ");
                        int searchChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (searchChoice) {
                            case 1:
                                System.out.print("Entrer le nom du client : ");
                                String surname = scanner.nextLine();
                                Client clientBySurname = clientService.surnameExist(surname);
                                if (clientBySurname != null) {
                                    System.out.println("Client trouvé :");
                                    System.out.println("Nom : " + clientBySurname.getSurname());
                                    System.out.println("Téléphone : " + clientBySurname.getTelephone());
                                    System.out.println("Adresse : " + clientBySurname.getAdresse());
                                } else {
                                    System.out.println("Aucun client trouvé avec ce nom.");
                                }
                                break;

                            case 2:
                                System.out.print("Entrer le numéro de téléphone : ");
                                String telephone = scanner.nextLine();
                                Client clientByTelephone = clientService.telephoneExist(telephone);
                                if (clientByTelephone != null) {
                                    System.out.println("Client trouvé :");
                                    System.out.println("Nom : " + clientByTelephone.getSurname());
                                    System.out.println("Téléphone : " + clientByTelephone.getTelephone());
                                    System.out.println("Adresse : " + clientByTelephone.getAdresse());
                                } else {
                                    System.out.println("Aucun client trouvé avec ce numéro de téléphone.");
                                }
                                break;

                            default:
                                System.out.println("Choix invalide.");
                        }
                        break;
                    case 5:
                        System.out.println("Créer une dette pour un client :");
                        System.out.print("Entrer l'ID du client pour lequel créer la dette : ");
                        int clientId = scanner.nextInt();
                        scanner.nextLine();

                        Client selectedClient = clientService.findById(clientId);
                        if (selectedClient == null) {
                            System.out.println("Aucun client trouvé avec cet ID.");
                            break;
                        }

                        System.out.println("Créer une dette pour le client : " + selectedClient.getSurname());
                        Dette dette = detteView.saisieDette();

                        dette.setClient(selectedClient);
                        detteService.create(dette);

                        System.out.println("Dette créée avec succès pour le client " + selectedClient.getSurname());
                        break;
                    case 6:
                        detteView.afficherDettesNonSoldees(detteRepository.findUnpaidDebtsByClientId(1));
                        break;
                    case 7:
                        System.out.println("Enregistrer un paiement pour une dette :");
                        System.out.print("Entrer l'ID de la dette : ");
                        int detteId = scanner.nextInt();
                        scanner.nextLine();

                        Dette selectedDette = detteService.findById(detteId);
                        if (selectedDette == null) {
                            System.out.println("Aucune dette trouvée avec cet ID.");
                            break;
                        }

                        System.out.println("Dette sélectionnée :");
                        System.out.println("Montant total : " + selectedDette.getMontant());
                        System.out.println("Montant versé : " + selectedDette.getMontantVerse());
                        System.out.println("Montant restant : " + selectedDette.getMontantRestant());

                        PaiementView paiementVieww = new PaiementView(scanner);
                        Paiement paiement = paiementView.saisiePaiement(selectedDette.getMontantRestant());

                        if (paiement == null) {
                            System.out.println("Le paiement n'a pas été enregistré. Vérifiez les montants.");
                            break;
                        }

                        selectedDette.addPaiement(paiement);
                        detteService.updateCumulMontantDus(selectedDette);

                        System.out.println("Paiement enregistré avec succès.");
                        System.out.println("Montant restant : " + selectedDette.getMontantRestant());
                        break;

                    case 8:
                        final DemandeDetteView demandeDetteVieww = new DemandeDetteView(scanner, demandeDetteService);
                        System.out.println("1 - Filtrer les demandes");
                        System.out.println("2 - Voir les articles d'une demande");
                        System.out.println("3 - Traiter une demande (Valider / Refutilisateur)");
                        System.out.print("Votre choix : ");
                        int choixDemande = scanner.nextInt();
                        scanner.nextLine();

                        switch (choixDemande) {
                            case 1:
                                demandeDetteVieww.afficherDemandesFiltrees();
                                break;
                            case 2:
                                demandeDetteVieww.afficherArticlesPourDemande();
                                break;
                            case 3:
                                demandeDetteVieww.traiterDemande();
                                break;
                            default:
                                System.out.println("Choix invalide.");
                        }
                        break;

                    case 9:
                        System.out.println("Au revoir !");
                        break;

                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } while (choix != 9);

        } else if (utilisateur.getRole() == Role.ADMIN) {
            do {
                System.out.println("1 - Créer un compte utilisateur à un client sans compte");
                System.out.println("2 - Créer un compte utilisateur avec un role Boutiquier ou admin");
                System.out.println("3 - Désactiver/Activer  un compte utilisateur");
                System.out.println("4 - Afficher les comptes utilisateurs  actifs ou par rôle.");
                System.out.println("5 - Créer/lister des articles et filtrer par disponibilité(qteStock!=0) ");
                System.out.println("6 - Mettre à jour la quantite en stock dun article");
                System.out.println("7 - Archiver les dettes soldées");
                System.out.println("8 - Quitter");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();
                scanner.nextLine();
                switch (choix) {
                    case 1:
                        System.out.println("Entrez l'ID du client pour lequel créer un compte utilisateur :");
                        int clientId = scanner.nextInt();
                        scanner.nextLine();

                        Utilisateur newUtilisateur = utilisateurView.saisieUtilisateur();
                        if (newUtilisateur != null) {
                            try {
                                clientRepository.creerComptePourClient(clientId, newUtilisateur);
                                System.out.println("Compte utilisateur créé et associé avec succès.");
                            } catch (Exception e) {
                                System.err.println("Une erreur s'est produite : " + e.getMessage());
                            }
                        }
                        break;
                    case 2:
                        Utilisateur utilisateurr = utilisateurView.saisieUtilisateur();
                        utilisateurRepository.creerCompteAvecRole(utilisateurr);
                        break;
                    case 3:
                        utilisateurView.afficherMenuGestionCompte();
                        break;
                    case 4:
                        utilisateurView.afficherComptesUtilisateurs();
                        break;
                    case 5:
                        articleView.gestionArticles();
                        break;
                    case 6:
                        System.out.println("Mettre à jour la quantité en stock d’un article.");
                        int[] data = articleView.saisirQuantiteStock();
                        int articleId = data[0];
                        int nouvelleQuantite = data[1];
                        articleRepository.updateQuantiteStock(articleId, nouvelleQuantite);
                        break;
                    case 7:
                        System.out.println("Archivage des dettes soldées...");
                        detteRepository.archiverDettesSoldees();
                        break;

                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }

            } while (choix != 8);
        } else if (utilisateur.getRole() == Role.CLIENT) {
            do {
                System.out.println("1 - Lister ses dettes non soldées");
                System.out.println("2 - Faire une demande de Dette");
                System.out.println("3 - Lister ses demandes de dette");
                System.out.println("4 - Envoyer une relance");
                System.out.println("5 - Quitter");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();
                scanner.nextLine();
                int clientId = scanner.nextInt();


                switch (choix) {
                    case 1:
                        System.out.println("Lister les dettes non soldées d’un client.");
                        System.out.println("Entrer votre ID de client :");

                        List<Dette> dettesNonSoldees = detteRepository.listerDettesNonSoldeesParClient(clientId);
                        detteView.afficherDettes(dettesNonSoldees);

                        System.out.println("Voulez-vous voir les détails pour une dette spécifique ? (o/n)");
                        char choice = scanner.next().charAt(0);
                        if (choice == 'o' || choice == 'O') {
                            System.out.println("Entrer l'ID de la dette :");
                            int detteId = scanner.nextInt();

                            System.out.println("1 - Voir les articles associés");
                            System.out.println("2 - Voir les paiements effectués");
                            int choixDetails = scanner.nextInt();

                            if (choixDetails == 1) {
                                List<Article> articles = articleRepository.listerArticlesParDette(detteId);
                                articleView.afficherArticles(articles);
                            } else if (choixDetails == 2) {
                                List<Paiement> paiements = paiementService.getPaiementsParDette(detteId);
                                paiementView.afficherPaiements(paiements);
                            } else {
                                System.out.println("Option invalide.");
                            }
                        }
                        break;
                    case 2:
                        demandeDetteView.creerDemandeDette();
                        break;
                    case 3:
                        System.out.println("Entrez l'état de la dette (En Cours ou Annulée) : ");
                        String etatFiltre = scanner.nextLine(); 

                        DemandeDetteRepositoryBd demandeDetteRepository = new DemandeDetteRepositoryBd();
                        List<Dette> demandes = demandeDetteRepository.readDemandesDetteParEtat(clientId, etatFiltre);

                        if (demandes.isEmpty()) {
                            System.out.println("Aucune demande de dette trouvée pour cet état.");
                        } else {
                            System.out.println("Demandes de dette " + etatFiltre + ":");
                            for (Dette dette : demandes) {
                                System.out.println("ID: " + dette.getId() + ", Montant: " + dette.getMontant()
                                        + ", Montant Restant: " + dette.getMontantRestant());
                            }
                        }
                        break;

                    case 4:
                        System.out.println("Entrez l'ID de la demande à relancer : ");
                        int demandeId = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Ajoutez un commentaire pour cette relance : ");
                        String commentaire = scanner.nextLine();

                        DemandeDetteService demandeService = new DemandeDetteServiceImpl(
                                new DemandeDetteRepositoryBd(),
                                new ArticleRepositoryBd());
                        demandeService.relancerDemandeAnnulee(demandeId, commentaire);
                        break;

                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");

                        break;
                }

            } while (choix != 5);
        } else {
            System.out.println("Accès non autorisé pour ce rôle.");
        }
        scanner.close();

    }

}