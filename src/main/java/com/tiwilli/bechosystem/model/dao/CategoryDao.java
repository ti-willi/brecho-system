package com.tiwilli.bechosystem.model.dao;

import com.tiwilli.bechosystem.model.entities.Category;

import java.util.List;

public interface CategoryDao {

    void insert(Category obj);
    void update(Category obj);
    void deleteById(Integer id);
    Category findById(Integer id);
    List<Category> findAll();
}
