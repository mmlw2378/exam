package com.dette.repository.list;

import com.dette.core.repository.Repository;
import com.dette.entities.Utilisateur;

public interface UtilisateurRepository extends Repository<Utilisateur> {

    public boolean loginExist(String login);
    Utilisateur seConnecter(String login, String password);
}