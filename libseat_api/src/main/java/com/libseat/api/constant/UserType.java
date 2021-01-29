package com.libseat.api.constant;

/**
 * 用户的类型
 * @author witch
 */

public enum UserType {
    /**体验*/
    EXPERIENCE(0),
    /**正式*/
    FORMAL(1);

    private Integer id;
    UserType(Integer id) {
        this.id = id;
    }
    public static UserType getById(int id) {
        for (UserType userType : UserType.values()) {
            if (userType.id == id) {
                return userType;
            }
        }
        return null;
    }
}
