package com.libseat.admin.model;

import com.libseat.api.entity.OrderEntity;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class OrderDetailModel{

    private OrderEntity order;

    private String orderType;

    private String defaultEvaluateTime;

    private String customerTel;

    private String customerMail;

    private String productName;

    private String productNo;

    private BigDecimal productPrice;

    private String stadiumAddr;
}
