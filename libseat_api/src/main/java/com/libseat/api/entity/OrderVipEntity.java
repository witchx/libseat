package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;


@Data
@Table(name = "lib_order_vip")
@Alias(value = "OrderVipEntity")
public class OrderVipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String no;
    @Column
    private Integer productId;
    @Column
    private Integer customerId;
    @Column
    private Integer userId;
    @Column
    private Timestamp createTime;
    @Column
    private Integer deleteFlag;
}
