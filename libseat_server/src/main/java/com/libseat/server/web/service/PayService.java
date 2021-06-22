package com.libseat.server.web.service;

import com.libseat.api.entity.PayEntity;
import com.libseat.server.web.dto.PayDto;
import com.libseat.server.web.dto.PayInfo;

public interface PayService {
    Boolean createPay(PayDto payDto);

    Integer insertPay(PayEntity payEntity);

    PayEntity getPayById(Integer id);

    PayInfo getPayByOrderId(Integer id);

    Object alipay(PayDto payDto);

    void fallback(String orderNo);

    void refund(String orderNo,String price);

}
