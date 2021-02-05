package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Table(name = "lib_vip_card")
@Alias(value = "VipCardEntity")
public class VipCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 货号*/
    @Column
    private String no;
    /** 商品名称*/
    @Column
    private String name;
    /** 用户id*/
    @Column
    private Integer userId;
    /** 可用次数*/
    @Column
    private Integer times;
    /** 原价*/
    @Column
    private BigDecimal originalPrice;
    /** 购买金额 */
    @Column
    private BigDecimal price;
    /** 值多少钱*/
    @Column
    private BigDecimal money;
    /** 商品类型*/
    @Column
    private Integer type;
    /** 具体描述 */
    @Column
    private String des;
    /** 使用期限 开始时间*/
    @Column
    private Timestamp startTime;
    /** 使用期限 结束时间*/
    @Column
    private Timestamp endTime;
    /** 创建时间*/
    @Column
    private Timestamp createTime;
    /** 修改时间*/
    @Column
    private Timestamp modifyTime;
    @Transient
    private Long sales;
    @Transient
    private String companyName;
}
