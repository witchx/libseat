package com.libseat.admin.service.impl;


import com.libseat.admin.mapper.SeatMapper;
import com.libseat.admin.service.SeatService;
import com.libseat.api.model.Row;
import com.libseat.api.model.Seat;
import com.libseat.api.entity.SeatEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatMapper seatMapper;

    @Override
    public List<SeatEntity> getSeatList(Integer roomId, Date startTime, Date endTime) {
        return seatMapper.getSeatList( roomId, startTime, endTime);
    }

    @Override
    public Integer updateSeat(SeatEntity seatEntity) {
        return seatMapper.updateByPrimaryKeySelective(seatEntity);
    }

    @Transactional
    @Override
    public Integer updateSeatList(List<SeatEntity> seatEntities) throws Exception  {
        Integer count = 0;
        for (SeatEntity seatEntity : seatEntities) {
            if (updateSeat(seatEntity) == 0) {
                throw new Exception("seat 更新操作失败");
            }
            count ++;
        }
        return count;
    }

    @Override
    public Integer insertSeat(SeatEntity seatEntity) {
        return seatMapper.insert(seatEntity);
    }

    @Transactional
    @Override
    public Integer insertSeatList(List<SeatEntity> seatEntities) throws Exception {
        Integer count = 0;
        for (SeatEntity seatEntity : seatEntities) {
            if (insertSeat(seatEntity) == 0) {
                throw new Exception("seat 插入操作失败");
            }
            count ++;
        }
        return count;
    }

    @Override
    public List<SeatEntity> assembleSeats(Seat[] seats, Integer roomId) {
        List<SeatEntity> seatEntities = new LinkedList<>();
        for (int i=0; i<seats.length; i++) {
            Row[] rows = seats[i].getRow();
            for (int j=0; j<rows.length; j++) {
                SeatEntity seatEntity = new SeatEntity();
                seatEntity.setId(rows[j] == null ? null: rows[j].getSeatId());
                seatEntity.setRoomId(roomId);
                seatEntity.setLine(i);
                seatEntity.setRow(j);
                seatEntity.setStatus(rows[j].getValue());
                seatEntities.add(seatEntity);
            }
        }
        return seatEntities;
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
    public void deleteSeat(SeatEntity seatEntity) {
        seatMapper.delete(seatEntity);
    }
}
