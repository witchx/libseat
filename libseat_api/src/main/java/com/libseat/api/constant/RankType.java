package com.libseat.api.constant;

public enum RankType {
    /** 周排名*/
    WEEK(1),
    /** 月排名*/
    MONTH(2),
    /** 年排名*/
    YEAR(3);

    private Integer id;
    RankType(Integer id) {
        this.id = id;
    }
    public static RankType getById(int id) {
        for (RankType rankType : RankType.values()) {
            if (rankType.id == id) {
                return rankType;
            }
        }
        return null;
    }
    public Integer getId() {
        return this.id;
    }
}
