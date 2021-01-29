package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_order_record")
@Alias(value = "OrderRecordEntity")
public class OrderRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer operationType;
    @Column
    private Integer operationId;
    @Column
    private String operationName;
    @Column
    private Integer orderId;
    @Column
    private Integer payId;
    @Column
    private String des;
    @Column
    private Timestamp createTime;
}
