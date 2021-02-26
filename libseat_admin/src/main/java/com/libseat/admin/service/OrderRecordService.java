package com.libseat.admin.service;

import com.libseat.api.entity.OrderRecordEntity;

import java.util.List;

public interface OrderRecordService {
    List<OrderRecordEntity> getOrderRecord(OrderRecordEntity orderRecordEntity);

    int insertOrderRecord(OrderRecordEntity orderRecordEntity);
}
