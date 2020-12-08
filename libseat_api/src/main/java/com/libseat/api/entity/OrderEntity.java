package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_order")
@Alias(value = "OrderEntity")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer seatId;
    @Column
    private Integer studentId;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp startTime;
    @Column
    private Timestamp endTime;
    /**
     * 0：已取消
     * 1：已预定
     * 2：已签到
     * 3；已签退
     */
    @Column
    private String status;
    @Column
    private String deleteFlag;

    @Transient
    private Long time;
}
