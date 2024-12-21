package com.dette.services.Impl;

import java.util.List;

import com.dette.repository.bd.RepositoryBdImpl;

public abstract class ServiceImpl<T> {
    protected RepositoryBdImpl<T> repository;

    public ServiceImpl(RepositoryBdImpl<T> repository) {
        this.repository = repository;
    }

    public void create(T entity) {
        repository.create(entity);
    }

    public List<T> get(Boolean hasAccount) {
        return repository.read(hasAccount);
    }

    public List<T> get() {
        return repository.read(false);
    }
}