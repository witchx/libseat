package com.libseat.api.entity;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class Order {
    @Transient
    private OrderEntity order;
    @Transient
    private CustomerEntity customer;
    @Transient
    private UserEntity user;
    @Transient
    private String stadiumAddr;
}
