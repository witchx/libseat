package com.libseat.server.web.dto;

import lombok.Data;

@Data
public class OrderDto {
    /** 用户id*/
    private Integer userId;
    /** 顾客id*/
    private Integer customerId;
    /** 场馆id*/
    private Integer stadiumId;
    /** 订单金额*/
    private String price;
    /** 订单类型 座位 vip 商品*/
    private Integer type;

    private String discount;

    /*座位*/

    /** 座位id*/
    private Integer seatId;
    /** 开始时间*/
    private Long startTime;
    /** 结束时间*/
    private Long endTime;

    /*VIP*/

    private Integer vipCardId;
}
