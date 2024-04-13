package com.tiwilli.bechosystem.model.services;

import com.tiwilli.bechosystem.model.dao.ClothesDao;
import com.tiwilli.bechosystem.model.dao.factories.DaoFactory;
import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;

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

    public List<Clothes> findByCategory(Category category) {
        return dao.findByCategory(category);
    }

    public List<Clothes> findBySales(Sales sales) {
        return dao.findBySales(sales);
    }
}
