package com.tiwilli.bechosystem.model.entities;

import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;

import java.util.Date;
import java.util.Objects;

public class Clothes {

    private Integer id;
    private String name;
    private String size;
    private Double purchaseValue;
    private Double salesValue;
    private Date purchaseDate;
    private Date salesDate;
    private Date postDate;
    private Integer status;

    private Category category;

    private Sales sales;

    public Clothes() {
    }

    public Clothes(Integer id, String name, String size, Double purchaseValue, Double salesValue, Date purchaseDate, Date salesDate, Date postDate, ClothesStatus status, Category category, Sales sales) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.purchaseValue = purchaseValue;
        this.salesValue = salesValue;
        this.purchaseDate = purchaseDate;
        this.salesDate = salesDate;
        this.postDate = postDate;
        setStatus(status);
        this.category = category;
        this.sales = sales;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(Double purchaseValue) {
        this.purchaseValue = purchaseValue;
    }

    public Double getSalesValue() {
        return salesValue;
    }

    public void setSalesValue(Double salesValue) {
        this.salesValue = salesValue;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public ClothesStatus getStatus() {
        return ClothesStatus.valueOf(status);
    }

    public void setStatus(ClothesStatus status) {
        if (status != null) {
            this.status = status.getCode();
        }
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clothes clothes = (Clothes) o;

        return Objects.equals(id, clothes.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Clothes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", purchaseValue=" + purchaseValue +
                ", salesValue=" + salesValue +
                ", purchaseDate=" + purchaseDate +
                ", salesDate=" + salesDate +
                ", postDate=" + postDate +
                ", status=" + status +
                ", category=" + category +
                ", sales=" + sales +
                '}';
    }
}
