package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;

@Data
@Table(name = "lib_customer_coupon")
@Alias(value = "CustomerCouponEntity")
public class CustomerCouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer customerId;
    @Column
    private Integer couponId;
    @Column
    private Integer type;
}
