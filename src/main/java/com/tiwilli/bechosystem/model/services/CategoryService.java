package com.tiwilli.bechosystem.model.services;

import com.tiwilli.bechosystem.dao.CategoryDao;
import com.tiwilli.bechosystem.dao.DaoFactory;
import com.tiwilli.bechosystem.model.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private CategoryDao dao = DaoFactory.createCategoryDao();

    public List<Category> findAll() {
        return dao.findAll();
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
}
