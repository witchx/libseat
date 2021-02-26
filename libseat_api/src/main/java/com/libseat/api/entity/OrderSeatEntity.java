package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 普通座位订单
 */
@Data
@Table(name = "lib_order_seat")
@Alias(value = "OrderSeatEntity")
public class OrderSeatEntity extends Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 订单id*/
    @Column
    private Integer orderId;
    /** 座位id*/
    @Column
    private Integer seatId;
    /** 开始时间*/
    @Column
    private Timestamp startTime;
    /** 结束时间*/
    @Column
    private Timestamp endTime;
    /** 删除标识*/
    @Column
    private Integer deleteFlag;
}
