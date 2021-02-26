package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Table(name = "lib_admin")
@Alias(value = "AdminEntity")
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String nickname;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String role;
    @Column
    private String icon;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp modifyTime;
    @Column
    private Timestamp lastLoginTime;
    @Column
    private String deleteFlag;
    @Transient
    private String roleName;
}
