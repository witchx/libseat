package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderRecordMapper;
import com.libseat.admin.service.OrderProductService;
import com.libseat.admin.service.OrderRecordService;
import com.libseat.api.entity.OrderRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  OrderRecordServiceImpl implements OrderRecordService {

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Override
    public List<OrderRecordEntity> getOrderRecord(OrderRecordEntity orderRecordEntity) {
        return orderRecordMapper.select(orderRecordEntity);
    }

    @Override
    public int insertOrderRecord(OrderRecordEntity orderRecordEntity) {
        return orderRecordMapper.insert(orderRecordEntity);
    }
}
