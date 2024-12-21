package com.dette.repository.list;

import com.dette.core.repository.Repository;
import com.dette.entities.Article;

public interface ArticleRepository extends Repository<Article>{

    void save(Article article);
    
}