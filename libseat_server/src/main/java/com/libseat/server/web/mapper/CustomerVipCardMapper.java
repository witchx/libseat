package com.libseat.server.web.mapper;

import com.libseat.api.entity.CustomerVipCardEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CustomerVipCardMapper extends MyBaseMapper<CustomerVipCardEntity> {
    List<VipCardEntity> getAllVipCardByCustomerId(@Param("id") Integer id);
}
