package com.tiwilli.bechosystem.model.dao;

import com.tiwilli.bechosystem.model.entities.ClientAddress;

import java.util.List;

public interface ClientAddressDao {

    void insert(ClientAddress obj);
    void update(ClientAddress obj);
    void deleteById(Integer id);
    ClientAddress findById(Integer id);
    List<ClientAddress> findAll();
}
