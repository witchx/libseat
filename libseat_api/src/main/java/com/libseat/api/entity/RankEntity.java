package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Data
@Table(name = "lib_rank")
@Alias(value = "RankEntity")
public class RankEntity {
    /**
    * 用户每次消费完时，需要马上更新bag,rank；
    * 每隔15分钟重新到数据库中取数据，可以存在Redis中，当Redis不存在时，再去数据库中取出
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer userId;

    @Column
    private Integer customerId;
    /**
     * 累计学时 周
     */
    @Column
    private Double hoursByWeek;
    /**
     * 累计天数 周
     */
    @Column
    private Integer daysByWeek;
    /**
     * 累计学时 月
     */
    @Column
    private Double hoursByMonth;
    /**
     * 累计天数 月
     */
    @Column
    private Integer daysByMonth;
    /**
     * 累计学时 年
     */
    @Column
    private Double hoursByYear;
    /**
     * 累计天数 年
     */
    @Column
    private Integer daysByYear;

    @Column
    private Timestamp modifyTime;
}
