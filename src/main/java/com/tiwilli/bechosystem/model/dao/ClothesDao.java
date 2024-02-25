package com.tiwilli.bechosystem.model.dao;

import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.entities.Clothes;

import java.util.List;

public interface ClothesDao {

    void insert(Clothes obj);
    void update(Clothes obj);
    void deleteById(Integer id);
    Clothes findById(Integer id);
    List<Clothes> findAll();
    List<Clothes> findByCategory(Category category);
}
