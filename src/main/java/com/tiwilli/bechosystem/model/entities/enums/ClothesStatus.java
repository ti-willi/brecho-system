package com.tiwilli.bechosystem.model.entities.enums;

public enum ClothesStatus {

    NOT_POSTED(1, "Não Postado"),
    POSTED(2, "Postado"),
    SOLD(3, "Vendido"),
    SHOPPING_BAG(4, "Sacolinha"),
    WAITING_SHIPMENT(5, "Aguardando envio"),
    SHIPPED(6, "Enviado");

    private int code;
    private String description;

    private ClothesStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ClothesStatus valueOf(int code) {
        for (ClothesStatus value : ClothesStatus.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid ClothesStatus code");
    }

    public static ClothesStatus fromDescription(String description) {
        for (ClothesStatus status : ClothesStatus.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ClothesStatus description");
    }
}
