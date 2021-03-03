package com.libseat.api.constant;

public enum SexType {
    /** 男*/
    MAN(1, "男"),
    /** 女*/
    WOMAN(2, "女");

    private Integer id;
    private String name;
    SexType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public static SexType getById(int id) {
        for (SexType sexType : SexType.values()) {
            if (sexType.id == id) {
                return sexType;
            }
        }
        return null;
    }
    public String getName() {
        return this.name;
    }
    public Integer getId() {
        return this.id;
    }
}
