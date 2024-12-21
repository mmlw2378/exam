package com.dette.services.Impl;

import com.dette.entities.Client;
import com.dette.entities.Utilisateur;
import com.dette.repository.bd.ClientRepositoryBd;
import com.dette.services.ClientService;

public class ClientServiceImpl extends ServiceImpl<Client> implements ClientService {

    public ClientServiceImpl(ClientRepositoryBd repository) {
        super(repository);
    }
 
    @Override
    public Client surnameExist(String surname) {
        return ((ClientRepositoryBd) repository).readClientBySurname(surname);
    }

    @Override
    public Client telephoneExist(String telephone) {
        return ((ClientRepositoryBd) repository).readClientByTelephone(telephone);
    }

    @Override
    public Client findById(int id) {
        return ((ClientRepositoryBd) repository).findById(id); 

    }


     public void createClientWithAccount(Client client, Utilisateur utilisateur) {
        ((ClientRepositoryBd) repository).createClientWithAccount(client, utilisateur);
    }

   

}