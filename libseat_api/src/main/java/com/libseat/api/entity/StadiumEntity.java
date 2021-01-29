package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_stadium")
@Alias(value = "StadiumEntity")
public class StadiumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String detailAddress;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp modifyTime;
    @Column
    private Integer hidden;
    @Column
    private Integer userId;
    @Transient
    private String companyName;
}
