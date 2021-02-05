package com.libseat.admin.service;

import com.libseat.api.entity.CouponEntity;
import com.libseat.api.entity.ProductEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface VipCardService {
    PageResult<VipCardEntity> getVipCardList(Integer id, String no, String name, Integer type, String companyName, Integer page, Integer pageSize);

    Integer insertVipCard(VipCardEntity vipCardEntity);

    Integer updateVipCard(VipCardEntity vipCardEntity);

    void deleteVipCardBatch(List<Integer> ids);

    void deleteVipCard(VipCardEntity vipCardEntity);
}
