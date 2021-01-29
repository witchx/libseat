package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Table(name = "lib_product")
@Alias(value = "ProductEntity")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 货号
     */
    private String no;
    private String name;
    private Integer type;
    private Integer userId;
    private Integer conType;
    /**
     * 可用次数
     */
    private Integer times;
    /**
     * 使用期限
     */
    private Timestamp startTime;
    private Timestamp endTime;
    /**
     * 值多少钱
     */
    private BigDecimal money;
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    /**
     * 具体购买的钱
     */
    private BigDecimal price;
    private String status;
    /**
     * 具体描述
     */
    private String des;
    private Timestamp createTime;
    private Long salesLimit;
    private Timestamp modifyTime;
    @Transient
    private Long sales;
    @Transient
    private String companyName;
}
