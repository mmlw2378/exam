package com.dette.repository.list;

import com.dette.entities.Client;
import com.dette.core.repository.Repository;

public interface ClientRepository extends Repository<Client> {
    public Client readClientBySurname(String surname);
    public Client readClientByTelephone(String telephone);
    public Client findById(int id);
}