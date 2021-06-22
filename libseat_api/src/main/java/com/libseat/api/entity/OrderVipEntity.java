package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;


/**
 * 会员卡订单
 */
@Data
@Table(name = "lib_order_vip")
@Alias(value = "OrderVipEntity")
public class OrderVipEntity extends Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** vip卡id*/
    @Column
    private Integer vipCardId;
    /** 订单id*/
    @Column
    private Integer orderId;
    /** 删除标志*/
    @Column
    private Integer deleteFlag;
    @Transient
    private VipCardEntity vipCard;
}
