package com.libseat.admin.mapper;

import com.libseat.api.entity.SeatEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface SeatMapper extends MyBaseMapper<SeatEntity> {

    List<SeatEntity> getSeatList(@Param("roomId") Integer roomId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
