package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 会员卡订单
 */
@Data
@Table(name = "lib_order_vip")
@Alias(value = "OrderVipEntity")
public class OrderVipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** vip卡id*/
    @Column
    private Integer vipCardId;
    /** 创建时间*/
    @Column
    private Timestamp createTime;
    /** 订单id*/
    @Column
    private String orderId;
    /** 删除标志*/
    @Column
    private Integer deleteFlag;
}