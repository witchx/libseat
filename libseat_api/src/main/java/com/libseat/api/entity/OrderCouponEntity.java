package com.libseat.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;



@Data
@Table(name = "lib_order_coupon")
@Alias(value = "OrderCouponEntity")
public class OrderCouponEntity extends Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer deleteFlag;
}
