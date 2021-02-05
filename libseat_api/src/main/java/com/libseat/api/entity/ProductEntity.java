package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 商品
 */
@Data
@Table(name = "lib_product")
@Alias(value = "ProductEntity")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 用户id*/
    @Column
    private Integer userId;
    /** 货号*/
    @Column
    private String no;
    /** 名称*/
    @Column
    private String name;
    /** 描述*/
    @Column
    private String des;
    /** 数量*/
    @Column
    private Integer num;
    /** 金额*/
    @Column
    private BigDecimal price;

    @Transient
    private String companyName;
}
