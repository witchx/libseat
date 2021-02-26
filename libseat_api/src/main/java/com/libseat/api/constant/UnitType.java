package com.libseat.api.constant;

public enum UnitType {
    /** 秒*/
    SECOND(1, "秒"),
    /** 分*/
    MINUTE(2, "分"),
    /** 时*/
    HOUR(3, "时"),
    /** 天"*/
    DAY(4, "天"),
    /** 周"*/
    WEEK(5, "周"),
    /** 月*/
    MONTH(6, "月"),
    /** 年*/
    YEAR(7, "年");

    private Integer id;
    private String des;
    UnitType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static UnitType getById(int id) {
        for (UnitType unitType : UnitType.values()) {
            if (unitType.id == id) {
                return unitType;
            }
        }
        return null;
    }
    public String getDes() {
        return this.des;
    }
}
