package com.libseat.admin.mapper;

import com.libseat.api.entity.OrderEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Component
public interface OrderMapper extends MyBaseMapper<OrderEntity> {
    List<OrderEntity> getOrderList(@Param("id") Integer id,
                                   @Param("no") String no,
                                   @Param("company") String company,
                                   @Param("customer") String customer,
                                   @Param("createStartTime") Timestamp createStartTime,
                                   @Param("createEndTime") Timestamp createEndTime,
                                   @Param("type") Integer type,
                                   @Param("progress") Integer progress,
                                   @Param("status") Integer status);
}
