package com.tiwilli.bechosystem.model.services;

import com.tiwilli.bechosystem.model.dao.ClientDao;
import com.tiwilli.bechosystem.model.dao.factories.DaoFactory;
import com.tiwilli.bechosystem.model.entities.Client;

import java.util.List;

public class ClientService {

    private ClientDao dao = DaoFactory.createClientDao();

    public List<Client> findAll() {
        return dao.findAll();
    }

    public Client findById(Integer id) {
        return dao.findById(id);
    }

    public void saveOrUpdate(Client obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public void remove(Client obj) {
        dao.deleteById(obj.getId());
    }

    public List<Client> findByName(String name) {
        return dao.findByName(name);
    }

}
