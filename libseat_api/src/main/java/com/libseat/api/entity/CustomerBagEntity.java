package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Data
@Table(name = "lib_customer_bag")
@Alias(value = "CustomerBagEntity")
public class CustomerBagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer customerId;

    /**
     * 储值 元
     */
    @Column
    private BigDecimal totalValue;

    /**
     * 次数 次
     */
    @Column
    private Integer totalTimes;

    /**
     * 期限 天
     */
    @Column
    private Integer totalDays;

    /**
     * 每周学时
     */
    @Column
    private Integer hoursByWeek;

    /**
     * 优惠劵 优惠劵状态类型 ： 优惠劵列表
     * 每次取出的时候判断一下未使用的优惠劵的状态，将未使用过期的优惠劵移动到过期key里面去
     */
    @Column
    private HashMap<Integer,List<CouponEntity>> couponMap;
}
