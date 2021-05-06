package com.libseat.server.web.mapper;


import com.libseat.api.entity.OrderEntity;
import com.libseat.api.mapper.MyBaseMapper;
import com.libseat.server.web.dto.OrderRecordInfo;
import com.libseat.server.web.dto.SeatOrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface OrderMapper extends MyBaseMapper<OrderEntity> {

    List<SeatOrderInfo> getAllSeatOrderList(@Param("customerId") Integer customerId);

    List<OrderRecordInfo> getAllOrderList(@Param("customerId") Integer customerId);
}
