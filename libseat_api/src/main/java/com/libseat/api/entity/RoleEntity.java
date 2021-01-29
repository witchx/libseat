package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_role")
@Alias(value = "RoleEntity")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String des;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp modifyTime;
    @Column
    private String deleteFlag;
}
