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
public class OrderSeatEntity {
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
    /** 0：已预定 1：已取消 2：已签到 3；已关闭 */
    @Column
    private Integer status;
    /** 结束时间*/
    @Column
    private Timestamp endTime;
    /** 签到时间*/
    @Column
    private Timestamp signTime;
    /** 评价时间*/
    @Column
    private Timestamp evaluateTime;
    /** 使用vip卡*/
    @Column
    private Integer vipCardId;
    /** 使用优惠劵*/
    @Column
    private Integer couponId;
    /** 删除标识*/
    @Column
    private Integer deleteFlag;
    /** 订单状态*/
    @Transient
    private String orderStatus;
}
