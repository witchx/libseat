package com.libseat.server.web.service;


import com.libseat.api.entity.SeatEntity;
import com.libseat.api.model.Seat;

import java.sql.Timestamp;
import java.util.List;

public interface SeatService {

    List<SeatEntity> getSeatList(Integer roomId, Timestamp startTime, Timestamp endTime);

    Integer updateSeat(SeatEntity seatEntity);

    Seat[] unparkSeat(List<SeatEntity> seatList, Integer line, Integer row);
}
