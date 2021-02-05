package com.libseat.admin.mapper;

import com.libseat.api.entity.OrderProductEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderProductMapper extends MyBaseMapper<OrderProductEntity> {
}
