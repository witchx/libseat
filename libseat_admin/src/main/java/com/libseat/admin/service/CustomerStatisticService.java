package com.libseat.admin.service;


import com.libseat.api.entity.CustomerStatisticsEntity;

public interface CustomerStatisticService {


    Integer insertCustomerStatistic(CustomerStatisticsEntity customerStatisticsEntity);

    CustomerStatisticsEntity getCustomerStatisticByCustomerId(Integer id);
}
