package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
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
    private String sex;
    @Column
    private String tel;
    @Column
    private String mail;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp modifyTime;
    @Column
    private Timestamp lastLoginTime;
    @Column
    private String deleteFlag;
}
