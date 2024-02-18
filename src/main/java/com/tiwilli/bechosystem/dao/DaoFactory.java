package com.tiwilli.bechosystem.dao;

import com.tiwilli.bechosystem.dao.impl.CategoryDaoJDBC;
import com.tiwilli.bechosystem.db.DB;

public class DaoFactory {

    public static CategoryDao createCategoryDao() {
        return  new CategoryDaoJDBC(DB.getConnection());
    }
}
