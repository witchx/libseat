package com.libseat.server.web.mapper;


import com.libseat.api.entity.CustomerEntity;
import com.libseat.api.mapper.MyBaseMapper;
import com.libseat.server.web.dto.CustomerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CustomerMapper extends MyBaseMapper<CustomerEntity> {
    CustomerInfo getCustomerDetail(CustomerEntity customerEntity);

    CustomerEntity getCustomerInfo(CustomerEntity customerEntity);
}
