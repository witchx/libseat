package com.libseat.admin.mapper;


import com.libseat.api.entity.CustomerBagEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CustomerBagMapper extends MyBaseMapper<CustomerBagEntity> {

    void updateCustomerBagBatch(@Param("customerBagEntities") List<CustomerBagEntity> customerBagEntities);
}
