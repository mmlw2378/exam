package com.dette.services;

import com.dette.entities.Utilisateur;

import java.util.List;


public interface UtilisateurService {
        public void create(Utilisateur utilisateur);

        public List<Utilisateur> get();
    
    
    }

