package com.libseat.server.web.mapper;


import com.libseat.api.entity.RoomEntity;
import com.libseat.api.mapper.MyBaseMapper;
import com.libseat.server.web.dto.RoomInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Component
public interface RoomMapper extends MyBaseMapper<RoomEntity> {
    List<RoomInfo> getRoomList(@Param(value = "stadiumId") Integer stadiumId,
                                @Param(value = "startTime") Timestamp startTime,
                                @Param(value = "endTime") Timestamp endTime);
}
