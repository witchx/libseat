package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_pay")
@Alias(value = "PayEntity")
public class PayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer userId;
    @Column
    private Integer customerId;
    @Column
    private String no;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp payTime;
    @Transient
    private String companyName;
}
