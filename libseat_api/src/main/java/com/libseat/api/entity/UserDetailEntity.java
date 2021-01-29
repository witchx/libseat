package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table(name = "lib_user_detail")
@Alias(value = "UserDetailEntity")
public class UserDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer userId;
    @Column
    private Integer yesVipCustomerNum;
    @Column
    private Integer vipCustomerNum;
    @Column
    private Integer yesCustomerNum;
    @Column
    private Integer customerNum;
    @Column
    private Integer yesOrderNum;
    @Column
    private Integer orderNum;
    @Column
    private BigDecimal yesTurnover;
    @Column
    private BigDecimal turnover;
    @Column
    private Integer stadiumNum;
}
