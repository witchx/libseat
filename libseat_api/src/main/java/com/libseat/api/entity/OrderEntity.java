package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 订单简略信息
 */
@Data
@Table(name = "lib_order")
@Alias(value = "OrderEntity")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 订单号*/
    @Column
    private String no;
    /** 支付id*/
    @Column
    private Integer payId;
    /** 用户id*/
    @Column
    private Integer userId;
    /** 顾客id*/
    @Column
    private Integer customerId;
    /** 场馆id*/
    @Column
    private Integer stadiumId;
    /** 订单金额*/
    @Column
    private BigDecimal price;
    /** 使用优惠劵*/
    @Column
    private Integer couponId;
    /** 折扣金额*/
    @Column
    private BigDecimal discount;
    /** 订单进度*/
    @Column
    private Integer progress;
    @Column
    private Integer status;
    /** 订单类型 座位 vip 商品*/
    @Column
    private Integer type;
    /** 每一个月逻辑删除,六个月物理删除 */
    @Column
    private Integer deleteFlag;
    /** 创建时间*/
    @Column
    private Timestamp createTime;
    /** 签到时间*/
    @Column
    private Timestamp confirmTime;
    /** 评价时间*/
    @Column
    private Timestamp evaluateTime;
    @Column
    private Integer evaluate;
    /** 支付时间*/
    @Transient
    private Timestamp payTime;
    /** 公司名称*/
    @Transient
    private String companyName;
    /** 顾客名称*/
    @Transient
    private String customerName;
    /** 顾客名称*/
    @Transient
    private String orderProgress;
    @Transient
    private String orderType;
    @Transient
    private BigDecimal coupon;
    @Transient
    private String orderStatus;
    @Transient
    private Timestamp startTime;
    @Transient
    private Integer paymentType;

    @Transient
    private OrderSeatEntity orderSeat;
    @Transient
    private VipCardEntity vipCard;
    @Transient
    private OrderProductEntity orderProduct;
    @Transient
    private ProductEntity product;
}
