package com.dette.services.Impl;

import java.util.List;

import com.dette.entities.Article;
import com.dette.repository.bd.ArticleRepositoryBd;
import com.dette.services.ArticleService;

public class ArticleServiceImpl extends ServiceImpl<Article> implements ArticleService{
    
    public ArticleServiceImpl(ArticleRepositoryBd repository) {
        super(repository);
    }

    @Override
    public List<Article> get(Boolean hasAccount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }
}