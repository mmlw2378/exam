package com.dette.views;

import java.util.List;
import java.util.Scanner;

import com.dette.entities.Client;

public class ClientView {

    private Scanner scanner ;

    
    public ClientView(Scanner scanner) {
        this.scanner = scanner;
    }

    public Client saisieClient() {
        Client client = new Client();
  
        System.out.println("Entrer le surname");
        client.setSurname(scanner.nextLine());
        System.out.println("Entrer le Telephone");
        client.setTelephone(scanner.nextLine());
        System.out.println("Entrer l'Adresse");
        client.setAdresse(scanner.nextLine());
        System.out.println("Cumul des montants dus : " + client.getCumulMontantDus());
        return client;
    }

    public void afficheClient(List<Client> clients) {
        for (Client cl : clients) {
            System.out.println(cl);
        }
    }
}