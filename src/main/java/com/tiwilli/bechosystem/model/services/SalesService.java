package com.tiwilli.bechosystem.model.services;

import com.tiwilli.bechosystem.model.dao.SalesDao;
import com.tiwilli.bechosystem.model.dao.factories.DaoFactory;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;

import java.util.List;

public class SalesService {

    private SalesDao dao = DaoFactory.createSalesDao();

    public List<Sales> findAll() {
        return dao.findAll();
    }

    public Sales findById(Integer id) {
        return dao.findById(id);
    }

    public void saveOrUpdate(Sales obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public void remove(Sales obj) {
        dao.deleteById(obj.getId());
    }

    public int setQuantity(List<Clothes> clothes) {
        int sum = 0;
        for (Clothes item : clothes) {
            sum += 1;
        }
        return sum;
    }

    public double setTotalAmount(List<Clothes> clothes) {
        double sum = 0;
        for (Clothes item : clothes) {
            sum += item.getSalesValue();
        }
        return sum;
    }

    public double setProfit(List<Clothes> clothes) {
        double sum = 0;
        for (Clothes item : clothes) {
            sum += (item.getSalesValue() - item.getPurchaseValue());
        }
        return sum;
    }

}
