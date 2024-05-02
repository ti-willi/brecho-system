package com.tiwilli.bechosystem.model.services;

import com.tiwilli.bechosystem.model.dao.CategoryDao;
import com.tiwilli.bechosystem.model.dao.factories.DaoFactory;
import com.tiwilli.bechosystem.model.entities.Category;

import java.util.List;

public class CategoryService {

    private CategoryDao dao = DaoFactory.createCategoryDao();

    public List<Category> findAll() {
        return dao.findAll();
    }

    public Category findById(Integer id) {
        return dao.findById(id);
    }

    public void saveOrUpdate(Category obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public void remove(Category obj) {
        dao.deleteById(obj.getId());
    }

    public List<Category> findByName(String name) {
        return dao.findByName(name);
    }
}
