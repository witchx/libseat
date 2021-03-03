package com.libseat.admin.mapper;


import com.libseat.api.entity.OrderCouponEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OrderCouponMapper extends MyBaseMapper<OrderCouponEntity> {

}
