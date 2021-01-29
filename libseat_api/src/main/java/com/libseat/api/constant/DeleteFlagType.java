package com.libseat.api.constant;

/**
 * 用户的类型
 * @author witch
 */

public enum DeleteFlagType {
    /**存在*/
    EXIST(0),
    /**删除*/
    CANCEL(1);

    private Integer id;
    DeleteFlagType(Integer id) {
        this.id = id;
    }
    public static DeleteFlagType getById(int id) {
        for (DeleteFlagType userType : DeleteFlagType.values()) {
            if (userType.id == id) {
                return userType;
            }
        }
        return null;
    }
    public Integer getId() {
        return this.id;
    }
}
