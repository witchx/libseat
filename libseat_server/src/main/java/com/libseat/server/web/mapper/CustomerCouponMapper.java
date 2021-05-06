package com.libseat.server.web.mapper;

import com.libseat.api.entity.CustomerCouponEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CustomerCouponMapper extends MyBaseMapper<CustomerCouponEntity> {
}
