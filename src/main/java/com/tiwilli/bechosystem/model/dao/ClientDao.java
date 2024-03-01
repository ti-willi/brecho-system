package com.tiwilli.bechosystem.model.dao;

import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.ClientAddress;

import java.util.List;

public interface ClientDao {

    void insert(Client obj);
    void update(Client obj);
    void deleteById(Integer id);
    Client findById(Integer id);
    List<Client> findAll();
}
