package com.libseat.api.constant;

public enum  OperationType {
    /** 管理员*/
    ADMIN(1),
    /** 用户*/
    USER(2),
    /** 顾客*/
    CUSTOMER(3);

    private Integer id;
    OperationType(Integer id) {
        this.id = id;
    }
    public static OperationType getById(int id) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.id == id) {
                return operationType;
            }
        }
        return null;
    }
}
