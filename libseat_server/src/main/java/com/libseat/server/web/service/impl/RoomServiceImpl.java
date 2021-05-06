package com.libseat.server.web.service.impl;

import com.libseat.api.entity.RoomEntity;
import com.libseat.server.web.dto.RoomInfo;
import com.libseat.server.web.mapper.RoomMapper;
import com.libseat.server.web.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;


    @Override
    public List<RoomInfo> getRoomList(Integer stadiumId, Timestamp startTime, Timestamp endTime) {
        return roomMapper.getRoomList(stadiumId, startTime, endTime);
    }

    @Override
    public RoomEntity getRoomById(RoomEntity roomEntity) {
        return roomMapper.selectByPrimaryKey(roomEntity);
    }

}
