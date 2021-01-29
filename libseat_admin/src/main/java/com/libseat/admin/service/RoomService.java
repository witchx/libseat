package com.libseat.admin.service;


import com.libseat.api.entity.RoomEntity;
import com.libseat.utils.page.PageResult;

public interface RoomService {

    PageResult<RoomEntity> getRoomList(String name, Integer stadiumId, String stadiumName, Integer page, Integer pageSize);

    PageResult<RoomEntity> getRoomListByStadiumId(Integer stadiumId, Integer page, Integer pageSize);

    RoomEntity getRoom (RoomEntity roomEntity);

    RoomEntity getRoomById (RoomEntity roomEntity);

    Integer updateRoom(RoomEntity roomEntity);

    Integer insertRoom(RoomEntity roomEntity);

    Integer insertRoomAndSeat(RoomEntity roomEntity) throws Exception;

    void deleteRoom(RoomEntity roomEntity);
}
