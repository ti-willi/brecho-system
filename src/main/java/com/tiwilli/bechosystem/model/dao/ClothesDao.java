package com.tiwilli.bechosystem.model.dao;

import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;

import java.util.List;

public interface ClothesDao {

    void insert(Clothes obj);
    void update(Clothes obj);
    void deleteById(Integer id);
    Clothes findById(Integer id);
    List<Clothes> findAll();
    List<Clothes> findByName(String name);
    List<Clothes> findBySize(String size);
    List<Clothes> findByStatus(int status);
    List<Clothes> findByCategory(String categoryName);
    List<Clothes> findBySales(Sales sales);

}
