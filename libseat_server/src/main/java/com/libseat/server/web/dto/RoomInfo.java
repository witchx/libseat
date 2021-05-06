package com.libseat.server.web.dto;

import lombok.Data;

@Data
public class RoomInfo {
    private Integer id;
    private String name;
    private Integer stadiumId;
    private Integer availableSeatCount;
    private Integer totalSeatCount;
}
