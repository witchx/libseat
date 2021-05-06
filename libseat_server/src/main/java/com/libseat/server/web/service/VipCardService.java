package com.libseat.server.web.service;

import com.libseat.api.entity.VipCardEntity;

import java.util.List;

public interface VipCardService {
    VipCardEntity getVipCardById(Integer vipCardId);

    List<VipCardEntity> getAllVipCardByUserId(Integer id);

    VipCardEntity getVipCardByOrderId(Integer id);

    List<VipCardEntity> getAllVipCardByCustomerId(Integer id);
}
