package com.libseat.admin.mapper;

import com.libseat.api.entity.RoomEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RoomMapper extends MyBaseMapper<RoomEntity> {
    List<RoomEntity> getRoomList(@Param("userId") Integer userId,
                                 @Param("name") String name,
                                 @Param("stadiumId") Integer stadiumId,
                                 @Param("stadiumName") String stadiumName);

    List<RoomEntity> getRoomListByStadiumId(@Param(value = "stadiumId") Integer stadiumId);
}
