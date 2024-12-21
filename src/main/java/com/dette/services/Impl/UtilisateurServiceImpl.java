package com.dette.services.Impl;

import com.dette.entities.Utilisateur;
import com.dette.repository.bd.UtilisateurRepositoryBd;
import com.dette.services.UtilisateurService;

public class UtilisateurServiceImpl extends ServiceImpl<Utilisateur> implements UtilisateurService {

    public UtilisateurServiceImpl(UtilisateurRepositoryBd repository) {
        super(repository);
    }

}