package com.libseat.api.entity;


import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_order_detail")
@Alias(value = "OrderDtailEntity")
public class OrderDtailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer seatId;
    @Column
    private Timestamp startTime;
    @Column
    private Timestamp endTime;
    @Column
    private
}
