package com.libseat.admin.service;

import com.libseat.api.entity.CouponEntity;
import com.libseat.api.entity.ProductEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface CouponService {

    PageResult<CouponEntity> getCouponList(Integer id, String no, String name, Integer type, String companyName, Integer page, Integer pageSize);

    Integer insertCoupon(CouponEntity couponEntity);

    Integer updateCoupon(CouponEntity couponEntity);

    void deleteCouponBatch(List<Integer> ids);

    void deleteCoupon(CouponEntity couponEntity);
}
