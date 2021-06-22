package com.libseat.server.web.service.impl;


import com.libseat.api.entity.SeatEntity;
import com.libseat.api.model.Row;
import com.libseat.api.model.Seat;
import com.libseat.server.web.mapper.SeatMapper;
import com.libseat.server.web.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatMapper seatMapper;

    @Override
    public List<SeatEntity> getSeatList(Integer roomId, Timestamp startTime, Timestamp endTime) {
        return seatMapper.getSeatList( roomId, startTime, endTime);
    }

    @Override
    public Integer updateSeat(SeatEntity seatEntity) {
        return seatMapper.updateByPrimaryKeySelective(seatEntity);
    }

    @Override
    public Seat[] unparkSeat(List<SeatEntity> seatList, Integer line, Integer row) {
        Iterator<SeatEntity> iterator = seatList.iterator();
        Seat[] seats = new Seat[line];
        for (int i=0; i<line ;i++) {
            Seat seat = new Seat();
            Row[] rows = new Row[row];
            for (int j=0; j<row ; j++) {
                Row r = new Row();
                SeatEntity seatEntity = iterator.next();
                r.setValue(seatEntity.getStatus());
                r.setSeatId(seatEntity.getId());
                r.setOrderId(seatEntity.getOrderId());
                r.setStartTime(seatEntity.getStartTime() == null ? null : seatEntity.getStartTime().getTime());
                r.setEndTime(seatEntity.getEndTime() == null ? null : seatEntity.getEndTime().getTime());
                rows[j] = r;
            }
            seat.setRow(rows);
            seats[i] = seat;
        }
        return seats;
    }

    @Override
    public Integer getSeatStatusBySeatId(Integer seatId,Timestamp startTime,Timestamp endTime) {
        return seatMapper.getSeatStatusBySeatId(seatId,startTime,endTime);
    }
}
