package com.tiwilli.bechosystem.model.entities.enums;

public enum ClothesStatus {

    AVAILABLE(1, "Disponível"),
    SOLD(2, "Vendido");

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
