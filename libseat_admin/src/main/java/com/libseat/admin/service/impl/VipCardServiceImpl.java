package com.libseat.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.ProductMapper;
import com.libseat.admin.mapper.VipCardMapper;
import com.libseat.admin.service.ProductService;
import com.libseat.admin.service.VipCardService;
import com.libseat.api.entity.CouponEntity;
import com.libseat.api.entity.ProductEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipCardServiceImpl implements VipCardService {

    @Autowired
    private VipCardMapper vipCardMapper;

    @Override
    public PageResult<VipCardEntity> getVipCardList(Integer id, String no, String name, Integer type, String companyName, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<VipCardEntity> vipCardList = vipCardMapper.getVipCardList(id, no, name, type, companyName);
        PageInfo pageInfo = new PageInfo(vipCardList);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }


    @Override
    public Integer insertVipCard(VipCardEntity vipCardEntity) {
        return vipCardMapper.insert(vipCardEntity);
    }

    @Override
    public Integer updateVipCard(VipCardEntity vipCardEntity) {
        return vipCardMapper.updateByPrimaryKeySelective(vipCardEntity);
    }

    @Override
    public void deleteVipCardBatch(List<Integer> ids) {
        vipCardMapper.deleteVipCardBatch(ids);
    }

    @Override
    public void deleteVipCard(VipCardEntity vipCardEntity) {
        vipCardMapper.deleteByPrimaryKey(vipCardEntity);
    }


}
