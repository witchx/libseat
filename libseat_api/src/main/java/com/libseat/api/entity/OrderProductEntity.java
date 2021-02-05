package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 商品订单
 */
@Data
@Table(name = "lib_order_product")
@Alias(value = "OrderProductEntity")
public class OrderProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 商品id*/
    @Column
    private Integer productId;
    /** 订单id*/
    @Column
    private String orderId;
    /** 删除标志*/
    @Column
    private Integer deleteFlag;
}
