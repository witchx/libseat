package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Table(name = "lib_user")
@Alias(value = "UserEntity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String companyName;
    @Column
    private String icon;
    @Column
    private String tel;
    @Column
    private String address;
    @Column
    private String email;
    @Column
    private Integer status;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp modifyTime;
    @Column
    private Timestamp lastLoginTime;
    @Column
    private Integer deleteFlag;
}
