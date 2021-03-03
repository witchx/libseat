package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Table(name = "lib_customer")
@Alias(value = "CustomerEntity")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String icon;
    @Column
    private String nickname;
    @Column
    private String username;
    @Column
    private String password;
    /**
     * 1：男
     * 2：女
     */
    @Column
    private Integer sex;
    @Column
    private String tel;
    @Column
    private String email;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp modifyTime;
    @Column
    private Timestamp lastLoginTime;
    @Column
    private Integer deleteFlag;
    @Column
    private Integer userId;
    @Column
    private String companyName;

    @Transient
    private BigDecimal totalValue;
    @Transient
    private Integer totalTimes;
    @Transient
    private Integer totalDays;
}
