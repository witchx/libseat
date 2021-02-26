package com.libseat.api.constant;

public enum  OperationType {
    /** 管理员*/
    ADMIN(1,"管理员"),
    /** 用户*/
    USER(2,"用户"),
    /** 顾客*/
    CUSTOMER(3,"顾客");

    private Integer id;
    private String des;
    OperationType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static OperationType getById(int id) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.id == id) {
                return operationType;
            }
        }
        return null;
    }
    public String getDes() {
        return this.des;
    }
    public Integer getId() {
        return this.id;
    }
}
