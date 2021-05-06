package com.libseat.server.web.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderInfo {
    private String money;
    private String price;
    private String discount;
    private Integer cardType;
    private Integer type;
    private Integer usefulLife;
    private Integer times;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer seatId;
}
