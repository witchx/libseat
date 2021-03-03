package com.libseat.admin.service.impl;


import com.libseat.admin.mapper.CustomerStatisticMapper;
import com.libseat.admin.service.CustomerStatisticService;
import com.libseat.api.entity.CustomerStatisticsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerStatisticServiceImpl implements CustomerStatisticService {

    @Autowired
    private CustomerStatisticMapper customerStatisticMapper;
    @Override
    public Integer insertCustomerStatistic(CustomerStatisticsEntity customerStatisticsEntity) {
        return customerStatisticMapper.insertSelective(customerStatisticsEntity);
    }

    @Override
    public CustomerStatisticsEntity getCustomerStatisticByCustomerId(Integer id) {
        CustomerStatisticsEntity customerStatisticsEntity = new CustomerStatisticsEntity();
        customerStatisticsEntity.setCustomerId(id);
        return customerStatisticMapper.selectOne(customerStatisticsEntity);
    }
}
