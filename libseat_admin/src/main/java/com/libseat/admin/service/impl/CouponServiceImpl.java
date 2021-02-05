package com.libseat.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.CouponMapper;
import com.libseat.admin.mapper.ProductMapper;
import com.libseat.admin.service.CouponService;
import com.libseat.admin.service.ProductService;
import com.libseat.api.entity.CouponEntity;
import com.libseat.api.entity.ProductEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;


    @Override
    public PageResult<CouponEntity> getCouponList(Integer id, String no, String name, Integer type, String companyName, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CouponEntity> couponList = couponMapper.getCouponList(id, no, name, type, companyName);
        PageInfo pageInfo = new PageInfo(couponList);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public Integer insertCoupon(CouponEntity couponEntity) {
        return couponMapper.insert(couponEntity);
    }

    @Override
    public Integer updateCoupon(CouponEntity couponEntity) {
        return couponMapper.updateByPrimaryKeySelective(couponEntity);
    }

    @Override
    public void deleteCouponBatch(List<Integer> ids) {
        couponMapper.deleteCouponBatch(ids);
    }

    @Override
    public void deleteCoupon(CouponEntity couponEntity) {
        couponMapper.deleteByPrimaryKey(couponEntity);
    }

}
