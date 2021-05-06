package com.libseat.server.web.service;

import com.libseat.api.entity.CustomerVipCardEntity;
import com.libseat.api.entity.VipCardEntity;

import java.util.List;

public interface CustomerVipCardService {
    void insertCustomerVipCard(CustomerVipCardEntity customerVipCardEntity);

    List<VipCardEntity> getAllVipCardByCustomerId(Integer id);
}
