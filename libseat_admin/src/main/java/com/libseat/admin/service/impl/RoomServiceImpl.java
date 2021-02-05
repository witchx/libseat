package com.libseat.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.RoomMapper;
import com.libseat.admin.service.RoomService;
import com.libseat.admin.service.SeatService;
import com.libseat.api.model.Row;
import com.libseat.api.model.Seat;
import com.libseat.api.entity.RoomEntity;
import com.libseat.api.entity.SeatEntity;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SeatService seatService;

    @Override
    public PageResult<RoomEntity> getRoomList(String name, Integer stadiumId, String stadiumName, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<RoomEntity> roomEntities = roomMapper.getRoomList(name, stadiumId, stadiumName);
        PageInfo pageInfo = new PageInfo(roomEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public PageResult<RoomEntity> getRoomListByStadiumId(Integer stadiumId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<RoomEntity> roomEntities = roomMapper.getRoomListByStadiumId(stadiumId);
        PageInfo pageInfo = new PageInfo(roomEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }


    @Override
    public RoomEntity getRoom(RoomEntity roomEntity) {
        return roomMapper.selectOne(roomEntity);
    }

    @Override
    public RoomEntity getRoomById(RoomEntity roomEntity) {
        return roomMapper.selectByPrimaryKey(roomEntity);
    }

    @Override
    public Integer updateRoom(RoomEntity roomEntity) {
        return roomMapper.updateByPrimaryKeySelective(roomEntity);
    }

    @Override
    public Integer insertRoom(RoomEntity roomEntity) {
        Seat[] seat = roomEntity.getSeat();
        roomEntity.setLine(seat.length);
        Row[] row = seat[0].getRow();
        roomEntity.setRow(row.length);
        if (roomMapper.insert(roomEntity) != 0) {
            RoomEntity room = getRoom(roomEntity);
            return room == null ? 0 : room.getId();
        }
        return 0;
    }

    @Transactional
    @Override
    public void deleteRoom(RoomEntity roomEntity) {
        roomMapper.deleteByPrimaryKey(roomEntity);
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setRoomId(roomEntity.getId());
        seatService.deleteSeat(seatEntity);
    }

    @Transactional
    @Override
    public Integer insertRoomAndSeat(RoomEntity roomEntity) throws Exception {
        Integer count = insertRoom(roomEntity);
        if (count == 0) {
            throw new Exception("room 插入操作失败");
        } else {
            seatService.insertSeatList(seatService.assembleSeats(roomEntity.getSeat(), roomEntity.getId()));
            return count;
        }
    }
}
