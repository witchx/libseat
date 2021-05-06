package com.libseat.server.web.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PayInfo {
    private Timestamp time;
    private String price;
    private Integer paymentType;
    private String no;
    private Integer type;
}
