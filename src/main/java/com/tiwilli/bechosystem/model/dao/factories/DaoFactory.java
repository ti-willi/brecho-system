package com.tiwilli.bechosystem.model.dao.factories;

import com.tiwilli.bechosystem.model.dao.*;
import com.tiwilli.bechosystem.model.dao.impl.*;
import com.tiwilli.bechosystem.db.DB;

public class DaoFactory {

    public static CategoryDao createCategoryDao() {
        return new CategoryDaoJDBC(DB.getConnection());
    }

    public static ClothesDao createClothesDao() {
        return new ClothesDaoJDBC(DB.getConnection());
    }

    public static ClientAddressDao createClientAddressDao() {
        return new ClientAddressDaoJDBC(DB.getConnection());
    }

    public static ClientDao createClientDao() {
        return new ClientDaoJDBC(DB.getConnection());
    }

    public static SalesDao createSalesDao() {
        return new SalesDaoJDBC(DB.getConnection());
    }
}
