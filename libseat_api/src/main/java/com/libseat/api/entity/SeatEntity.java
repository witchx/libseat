package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Alias(value = "SeatEntity")
@Table(name = "lib_seat")
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
    /**
     * 1：可用
     * 0：无用
     */
    @Column
    private String status;
    @Column
    private String deleteFlag;
    @Transient
    private Integer orderId;
    @Transient
    private Timestamp startTime;
    @Transient
    private Timestamp endTime;
}
