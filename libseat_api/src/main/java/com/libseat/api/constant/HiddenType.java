package com.libseat.api.constant;

/**
 * 用户的类型
 * @author witch
 */

public enum HiddenType {
    /**显现*/
    SHOW(0),
    /**隐藏*/
    HIDE(1);

    private Integer id;
    HiddenType(Integer id) {
        this.id = id;
    }
    public static HiddenType getById(int id) {
        for (HiddenType userType : HiddenType.values()) {
            if (userType.id == id) {
                return userType;
            }
        }
        return null;
    }
}
