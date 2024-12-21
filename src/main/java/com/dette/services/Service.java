package com.dette.services;

import java.util.List;

public interface Service<T> {
    void create(T entity);

    List<T> get(Boolean hasAccount);

    List<T> get(); 
}