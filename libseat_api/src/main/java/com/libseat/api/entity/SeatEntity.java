package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_seat")
@Alias(value = "SeatEntity")
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer roomId;
    @Column
    private Integer row;
    @Column
    private Integer line;
    @Column
    private Integer status;
    @Transient
    private Integer orderId;
    @Transient
    private Timestamp startTime;
    @Transient
    private Timestamp endTime;
}

