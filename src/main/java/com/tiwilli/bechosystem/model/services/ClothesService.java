package com.tiwilli.bechosystem.model.services;

import com.tiwilli.bechosystem.model.dao.ClothesDao;
import com.tiwilli.bechosystem.model.dao.factories.DaoFactory;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;

import java.util.List;

public class ClothesService {

    private ClothesDao dao = DaoFactory.createClothesDao();

    public List<Clothes> findAll() {
        return dao.findAll();
    }

    public Clothes findById(Integer id) {
        return dao.findById(id);
    }

    public void saveOrUpdate(Clothes obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public void remove(Clothes obj) {
        dao.deleteById(obj.getId());
    }

    public List<Clothes> findByName(String name) {
        return dao.findByName(name);
    }

    public List<Clothes> findBySize(String size) {
        return dao.findBySize(size);
    }

    public List<Clothes> findByStatus(int status) {
        return dao.findByStatus(status);
    }

    public List<Clothes> findByCategory(String category) {
        return dao.findByCategory(category);
    }

    public List<Clothes> findBySales(Sales sales) {
        return dao.findBySales(sales);
    }
}
