package com.tiwilli.bechosystem.model.services;

import com.tiwilli.bechosystem.model.dao.ClientAddressDao;
import com.tiwilli.bechosystem.model.dao.DaoFactory;
import com.tiwilli.bechosystem.model.entities.ClientAddress;

import java.util.List;

public class ClientAddressService {

    private ClientAddressDao dao = DaoFactory.createClientAddressDao();

    public List<ClientAddress> findAll() {
        return dao.findAll();
    }

    public ClientAddress findById(Integer id) {
        return dao.findById(id);
    }

    public void saveOrUpdate(ClientAddress obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public void remove(ClientAddress obj) {
        dao.deleteById(obj.getId());
    }

}
