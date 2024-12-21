package com.dette.services;

import com.dette.entities.Client;
import com.dette.entities.Utilisateur;



public interface ClientService extends Service <Client> {
    public Client surnameExist(String surname);
        Client telephoneExist(String telephone);
    public Client findById(int id);
        void createClientWithAccount(Client client, Utilisateur utilisateur);
}

