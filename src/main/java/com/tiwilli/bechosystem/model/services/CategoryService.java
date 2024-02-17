package com.tiwilli.bechosystem.model.services;

import com.tiwilli.bechosystem.model.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        list.add(new Category(1L, "Calça"));
        list.add(new Category(2L, "Jaqueta"));
        list.add(new Category(3L, "Blusa"));
        list.add(new Category(4L, "Vestido"));
        list.add(new Category(4L, "Bota"));
        return list;
    }
}
