package com.tiwilli.bechosystem.model.entities;

import java.util.Objects;

public class ClientAddress {

    private Integer id;
    private String state;
    private String city;
    private String district;
    private String street;
    private String addressComplement;
    private Integer number;
    private String zipCode;


    public ClientAddress() {
    }

    public ClientAddress(Integer id, String state, String city, String district, String street, String addressComplement, Integer number, String zipCode) {
        this.id = id;
        this.state = state;
        this.city = city;
        this.district = district;
        this.street = street;
        this.addressComplement = addressComplement;
        this.number = number;
        this.zipCode = zipCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddressComplement() {
        return addressComplement;
    }

    public void setAddressComplement(String addressComplement) {
        this.addressComplement = addressComplement;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientAddress that = (ClientAddress) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientAddress{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", addressComplement='" + addressComplement + '\'' +
                ", number=" + number +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
