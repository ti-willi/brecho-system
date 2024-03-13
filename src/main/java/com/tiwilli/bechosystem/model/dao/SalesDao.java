package com.tiwilli.bechosystem.model.dao;

import com.tiwilli.bechosystem.model.entities.Sales;

import java.util.List;

public interface SalesDao {

    void insert(Sales obj);
    void update(Sales obj);
    void deleteById(Integer id);
    Sales findById(Integer id);
    List<Sales> findAll();
}
