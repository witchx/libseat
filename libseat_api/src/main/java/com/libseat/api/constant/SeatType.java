package com.libseat.api.constant;

/**
 * 座位状态类型
 * @author witch
 */

public enum SeatType {
    /** 无座*/
    EMPTY(0),
    /** 有座*/
    EXIST(1),
    /** 占用*/
    OCCUPY(2);

    private Integer id;
    SeatType(Integer id) {
        this.id = id;
    }
    public static SeatType getById(int id) {
        for (SeatType seatType : SeatType.values()) {
            if (seatType.id == id) {
                return seatType;
            }
        }
        return null;
    }
}
