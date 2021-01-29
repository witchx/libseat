package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_menu")
@Alias(value = "MenuEntity")
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer parentId;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp modifyTime;
    @Column
    private String title;
    @Column
    private Integer level;
    @Column
    private Integer sort;
    @Column
    private String name;
    @Column
    private String icon;
    @Column
    private Integer hidden;
    @Column
    private String access;
}
