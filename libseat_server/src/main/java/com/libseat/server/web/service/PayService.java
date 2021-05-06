package com.libseat.server.web.service;

import com.libseat.api.entity.PayEntity;
import com.libseat.server.web.dto.PayDto;
import com.libseat.server.web.dto.PayInfo;

public interface PayService {
    void createPay(PayDto payDto) throws Exception;

    Integer insertPay(PayEntity payEntity);

    PayEntity getPayById(Integer id);

    PayInfo getPayByOrderId(Integer id);
}
