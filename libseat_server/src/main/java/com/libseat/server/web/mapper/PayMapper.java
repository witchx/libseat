package com.libseat.server.web.mapper;

import com.libseat.api.entity.PayEntity;
import com.libseat.api.mapper.MyBaseMapper;
import com.libseat.server.web.dto.PayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface PayMapper extends MyBaseMapper<PayEntity> {
    PayInfo getPayByOrderId(@Param("id") Integer id);
}
