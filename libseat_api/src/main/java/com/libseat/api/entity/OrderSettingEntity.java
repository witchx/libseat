package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;

@Data
@Table(name = "lib_order_setting")
@Alias(value = "OrderSettingEntity")
public class OrderSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 订单逻辑删除时间 天
     */
    @Column
    private Integer logicalDeleteTime;
    /**
     * 订单物理删除时间 天
     */
    @Column
    private Integer physicalDeleteTime;
    /**
     * 默认好评时间
     */
    @Column
    private Integer evaluateTime;
    /**
     * 订单关闭时间 天 ：在order endTime之后的时间
     */
    @Column
    private Integer turnOffTime;

}
