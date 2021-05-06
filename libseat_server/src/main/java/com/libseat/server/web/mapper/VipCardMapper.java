package com.libseat.server.web.mapper;

import com.libseat.api.entity.VipCardEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface VipCardMapper  extends MyBaseMapper<VipCardEntity> {
    VipCardEntity getVipCardByOrderId(@Param("id") Integer id);
}
