package com.libseat.server.web.service;

import com.libseat.api.entity.RoomEntity;
import com.libseat.server.web.dto.RoomInfo;

import java.sql.Timestamp;
import java.util.List;

public interface RoomService {
    List<RoomInfo> getRoomList(Integer stadiumId, Timestamp startTime, Timestamp endTime);

    RoomEntity getRoomById(RoomEntity roomEntity);
}
