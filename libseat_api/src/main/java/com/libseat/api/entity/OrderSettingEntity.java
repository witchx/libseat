package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_order_setting")
@Alias(value = "OrderSettingEntity")
public class OrderSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String prefixDes;
    @Column
    private String suffixDes;
    @Column
    private Integer time;
    @Column
    private Boolean onOff;
    @Column
    private Timestamp modifyTime;
    @Column
    private Integer unit;
    @Transient
    private String unitName;
}
