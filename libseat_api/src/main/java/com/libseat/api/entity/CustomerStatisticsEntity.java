package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;

/**
 * 顾客统计数据
 */
@Data
@Table(name = "lib_customer_statistics")
@Alias(value = "CustomerStatisticsEntity")
public class CustomerStatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer customerId;
    /**
     * 累计学时
     */
    @Column
    private Long totalHours;

    /**
     * 累计天数
     */
    @Column
    private Long totalDays;

    /**
     * 周排行
     */
    @Column
    private Integer weekRank;
    /**
     * 月排行
     */
    @Column
    private Integer monthRank;
    /**
     * 年排行
     */
    @Column
    private Integer yearRank;
}
