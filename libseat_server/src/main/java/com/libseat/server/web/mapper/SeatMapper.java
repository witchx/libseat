package com.libseat.server.web.mapper;


import com.libseat.api.entity.SeatEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Component
public interface SeatMapper extends MyBaseMapper<SeatEntity> {

    List<SeatEntity> getSeatList(@Param("roomId") Integer roomId, @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);

    Integer getSeatStatusBySeatId(@Param("seatId") Integer seatId, @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);
}
