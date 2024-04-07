package com.tiwilli.bechosystem.model.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Sales {

    private Integer id;
    private Date salesDate;
    private Integer quantity;
    private Double totalAmount;
    private Double profit;

    private Client client;

    private List<Clothes> clothes = new ArrayList<>();

    public Sales() {
    }

    public Sales(Integer id, Date salesDate, Integer quantity, Double totalAmount, Double profit, Client client) {
        this.id = id;
        this.salesDate = salesDate;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.profit = profit;
        this.client = client;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
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
