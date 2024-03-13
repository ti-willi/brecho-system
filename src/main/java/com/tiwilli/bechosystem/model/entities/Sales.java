package com.tiwilli.bechosystem.model.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Sales {

    private Integer id;

    private Date salesDate;

    private Integer quantity;

    private Client client;

    private List<Clothes> clothes = new ArrayList<>();

    public Sales() {
    }

    public Sales(Integer id, Date salesDate, Integer quantity, Client client) {
        this.id = id;
        this.client = client;
        this.quantity = quantity;
        this.salesDate = salesDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setClothes(List<Clothes> clothes) {
        this.clothes = clothes;
    }

    public List<Clothes> getClothes() {
        return clothes;
    }

    public double getTotalAmount() {
        double sum = 0;
        for (Clothes item : clothes) {
            sum += item.getSalesValue();
        }
        return sum;
    }

    public double getProfit() {
        double sum = 0;
        for (Clothes item : clothes) {
            sum += (item.getSalesValue() - item.getPurchaseValue());
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sales sales = (Sales) o;

        return Objects.equals(id, sales.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "id=" + id +
                ", salesDate=" + salesDate +
                ", quantity=" + quantity +
                ", client=" + client +
                ", clothes=" + clothes +
                '}';
    }
}
