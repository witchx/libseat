package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Table(name = "lib_coupon")
@Alias(value = "CouponEntity")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String no;
    /** 名称 */
    @Column
    private String name;
    /** 优惠劵的面值 */
    @Column
    private BigDecimal price;
    /** 开始时间 */
    @Column
    private Timestamp startTime;
    /** 结束时间 */
    @Column
    private Timestamp endTime;
    /** 描述 */
    @Column
    private String des;
    /** 类型 */
    @Column
    private Integer type;
    /** 限量参数 */
    @Column
    private Long maxNum;
    @Column
    private Integer userId;
    @Transient
    private String companyName;
}
