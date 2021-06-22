package com.libseat.api.entity;

import com.libseat.api.model.RankNode;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table(name = "lib_customer_bag")
@Alias(value = "CustomerBagEntity")
public class CustomerBagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer customerId;

    /**
     * 储值 元
     */
    @Column
    private BigDecimal totalValue;

    /**
     * 次数 次
     */
    @Column
    private Integer totalTimes;

    /**
     * 期限 天
     */
    @Column
    private Integer totalDays;

    /**
     * 优惠劵 张
     */
    @Column
    private Integer totalCoupon;


    @Transient
    private RankEntity rank;

    @Transient
    private RankNode rankWeek;

    @Transient
    private RankNode rankMonth;

    @Transient
    private RankNode rankYear;

    public void init() {
        this.setTotalTimes(0);
        this.setTotalDays(0);
        this.setTotalCoupon(0);
        this.setTotalValue(BigDecimal.ZERO);
    }
}
