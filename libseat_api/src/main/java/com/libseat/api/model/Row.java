package com.libseat.api.model;

import lombok.Data;

@Data
public class Row {
    /**
     * 0代表此处为无座，1代表空座，2代表已被占用
     */
    private Integer value;
    private Integer orderId;
    private Integer seatId;
    private Long startTime;
    private Long endTime;
}