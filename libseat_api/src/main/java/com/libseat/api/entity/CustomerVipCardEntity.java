package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;

@Data
@Table(name = "lib_customer_vip_card")
@Alias(value = "CustomerVipCardEntity")
public class CustomerVipCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer customerId;
    @Column
    private Integer vipCardId;
    @Column
    private Integer type;
}
