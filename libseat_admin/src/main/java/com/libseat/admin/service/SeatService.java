package com.libseat.admin.service;

import com.libseat.api.dto.Seat;
import com.libseat.api.entity.SeatEntity;

import java.util.Date;
import java.util.List;

public interface SeatService {

    List<SeatEntity> getSeatList(Integer roomId, Date startTime, Date endTime);

    Integer updateSeat(SeatEntity seatEntity);

    Integer updateSeatList(List<SeatEntity> seatEntities) throws Exception;

    Integer insertSeat(SeatEntity seatEntity);

    Integer insertSeatList(List<SeatEntity> seatEntities) throws Exception;

    List<SeatEntity> assembleSeats(Seat[] seats, Integer roomId);

    Seat[] unparkSeat(List<SeatEntity> seatList, Integer line, Integer row);

    void deleteSeat(SeatEntity seatEntity);
}
