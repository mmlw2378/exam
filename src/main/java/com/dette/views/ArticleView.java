package com.dette.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.dette.core.bd.DatabaseImpl;
import com.dette.entities.Article;

public class ArticleView {

    private Scanner scanner;

    public ArticleView(Scanner scanner) {
        this.scanner = scanner;
    }
    // public void afficherArticle(Article article) {
    //     System.out.println("Nom de l'article: " + article.getNom());
    //     System.out.println("Prix de l'article: " + article.getPrix());
    // }

    // public Article creerArticle(Scanner scanner) {
    //     Article article = new Article();
    //     System.out.println("Entrer le nom de l'article");
    //     article.setNom(scanner.nextLine());
    //     System.out.println("Entrer le prix de l'article");
    //     article.setPrix(Double.parseDouble(scanner.nextLine()));
    //     return article;
    // }

    public void gestionArticles() {
        System.out.println("1 - Créer un article");
        System.out.println("2 - Lister tous les articles");
        System.out.println("3 - Lister les articles disponibles");
        System.out.print("Entrez votre choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); 

        switch (choix) {
            case 1:
                creerArticle();
                break;
            case 2:
                listerArticles(false); 
                break;
            case 3:
                listerArticles(true); 
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }

    public void creerArticle() {
        System.out.print("Entrez le nom de l'article : ");
        String nom = scanner.nextLine();

        System.out.print("Entrez le prix : ");
        double prix = scanner.nextDouble();

        System.out.print("Entrez la quantité en stock : ");
        int quantiteStock = scanner.nextInt();

        Article article = new Article(0,nom,0 prix,quantiteStock,0);
        String sql = "INSERT INTO article (nom, prix, quantitestock) VALUES (?, ?, ?)";

        DatabaseImpl database = new DatabaseImpl();
        database.OpenConnection();

        try {
            database.initPreparedStatement(sql);
            database.statement.setString(1, article.getNom());
            database.statement.setDouble(2, article.getPrix());
            database.statement.setInt(3, article.getQuantiteEnStock());

            int rows = database.statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Article créé avec succès !");
            } else {
                System.out.println("Échec de la création de l'article.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.CloseConnection();
        }
    }

    public void listerArticles(boolean uniquementDisponibles) {
        String sql = uniquementDisponibles 
            ? "SELECT * FROM article WHERE quantitestock > 0" 
            : "SELECT * FROM article";

        DatabaseImpl database = new DatabaseImpl();
        database.OpenConnection();

        try {
            database.initPreparedStatement(sql);
            ResultSet resultSet = database.statement.executeQuery();

            System.out.println("Liste des articles :");
            while (resultSet.next()) {
                System.out.println(
                    "ID : " + resultSet.getInt("id") +
                    ", Nom : " + resultSet.getString("nom") +
                    ", Prix : " + resultSet.getDouble("prix") +
                    ", Quantité en stock : " + resultSet.getInt("quantiteStock")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.CloseConnection();
        }
    }


    public int[] saisirQuantiteStock() {
        System.out.println("Entrer l'ID de l'article : ");
        int id = scanner.nextInt();
        System.out.println("Entrer la nouvelle quantité en stock : ");
        int quantite = scanner.nextInt();
        return new int[]{id, quantite}; 
    }


    public void afficherArticles(List<Article> articles) {
    for (Article article : articles) {
        System.out.println(article);
    }
}

    
}