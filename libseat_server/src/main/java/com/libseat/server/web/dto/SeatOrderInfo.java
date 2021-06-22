package com.libseat.server.web.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SeatOrderInfo {
    private String roomName;
    /** 1-未消费：1、2  2-已消费：3、4  3-已取消：5、6*/
    private Integer orderId;
    private Integer type;
    private Integer seatId;
    private Timestamp startTime;
    private Timestamp endTime;
}
