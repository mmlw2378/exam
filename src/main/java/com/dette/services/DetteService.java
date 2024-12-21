package com.dette.services;

import com.dette.entities.Dette;

public interface DetteService extends Service <Dette>{
    Dette creerDette(int idClient);
    void updateCumulMontantDus(Dette selectedsDette);

    public Dette findById(int detteId);
}
