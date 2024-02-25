package com.tiwilli.bechosystem.model.dao;

import com.tiwilli.bechosystem.model.dao.impl.CategoryDaoJDBC;
import com.tiwilli.bechosystem.model.dao.impl.ClothesDaoJDBC;
import com.tiwilli.bechosystem.db.DB;

public class DaoFactory {

    public static CategoryDao createCategoryDao() {
        return new CategoryDaoJDBC(DB.getConnection());
    }

    public static ClothesDao createClothesDao() {
        return new ClothesDaoJDBC(DB.getConnection());
    }
}
