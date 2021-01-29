package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Table(name = "lib_order")
@Alias(value = "OrderEntity")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String no;
    @Column
    private BigDecimal price;
    /**
     * 0：已预定
     * 1：已取消
     * 2：已签到
     * 3；已关闭
     */
    @Column
    private Integer status;
    @Column
    private Integer progress;
    @Column
    private Integer type;
    /**
     * 每一个月逻辑删除,六个月物理删除
     */
    @Column
    private Integer deleteFlag;
    @Column
    private Integer seatId;
    @Column
    private Integer userId;
    @Column
    private Integer customerId;
    @Column
    private Integer vipOrderId;
    @Column
    private Timestamp startTime;
    @Column
    private Timestamp endTime;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp payTime;
    @Column
    private Timestamp signTime;
    @Column
    private Timestamp evaluateTime;
    @Transient
    private String companyName;
    @Transient
    private String customerName;
    @Transient
    private String orderStatus;

}
