package com.libseat.server.web.mapper;


import com.libseat.api.entity.CustomerBagEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CustomerBagMapper extends MyBaseMapper<CustomerBagEntity> {

}
