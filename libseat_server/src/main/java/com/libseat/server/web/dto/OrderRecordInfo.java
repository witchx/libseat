package com.libseat.server.web.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderRecordInfo {
    private String price;
    private Integer orderType;
    private Integer orderStatus;
    private Timestamp orderTime;
    private Integer orderId;
}
