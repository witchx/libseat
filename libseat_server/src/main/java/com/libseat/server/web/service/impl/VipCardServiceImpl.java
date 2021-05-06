package com.libseat.server.web.service.impl;

import com.libseat.api.entity.VipCardEntity;
import com.libseat.server.web.mapper.VipCardMapper;
import com.libseat.server.web.service.CustomerVipCardService;
import com.libseat.server.web.service.VipCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipCardServiceImpl implements VipCardService {
    @Autowired
    private VipCardMapper vipCardMapper;
    @Autowired
    private CustomerVipCardService customerVipCardService;
    @Override
    public VipCardEntity getVipCardById(Integer id) {
        VipCardEntity vipCardEntity = new VipCardEntity();
        vipCardEntity.setId(id);
        return vipCardMapper.selectByPrimaryKey(vipCardEntity);
    }

    @Override
    public List<VipCardEntity> getAllVipCardByUserId(Integer id) {
        VipCardEntity vipCardEntity = new VipCardEntity();
        vipCardEntity.setUserId(id);
        return vipCardMapper.select(vipCardEntity);
    }

    @Override
    public VipCardEntity getVipCardByOrderId(Integer id) {
        return vipCardMapper.getVipCardByOrderId(id);
    }

    @Override
    public List<VipCardEntity> getAllVipCardByCustomerId(Integer id) {
        return customerVipCardService.getAllVipCardByCustomerId(id);
    }

}
