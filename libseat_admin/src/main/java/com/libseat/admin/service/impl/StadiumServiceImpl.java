package com.libseat.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.StadiumMapper;
import com.libseat.admin.service.StadiumService;
import com.libseat.api.entity.StadiumEntity;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StadiumServiceImpl implements StadiumService {
    @Autowired
    private StadiumMapper stadiumMapper;

    @Override
    public PageResult<StadiumEntity> getStadiumList(Integer userId, String name, String companyName, String address, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<StadiumEntity> stadiumEntities = stadiumMapper.getStadiumList(userId, name, companyName, address);
        PageInfo pageInfo = new PageInfo(stadiumEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public StadiumEntity getStadium(StadiumEntity stadiumEntity) {
        return stadiumMapper.selectOne(stadiumEntity);
    }

    @Override
    public Integer updateStadium(StadiumEntity stadiumEntity) {
        return stadiumMapper.updateByPrimaryKeySelective(stadiumEntity);
    }

    @Override
    public Integer insertStadium(StadiumEntity stadiumEntity) {
        return stadiumMapper.insert(stadiumEntity);
    }

    @Override
    public Integer deleteStadium(StadiumEntity stadiumEntity) {
        return stadiumMapper.deleteByPrimaryKey(stadiumEntity);
    }

    @Override
    public List<StadiumEntity> getStadiumByUserId(Integer userId) {
        StadiumEntity stadiumEntity = new StadiumEntity();
        stadiumEntity.setUserId(userId);
        return stadiumMapper.select(stadiumEntity);
    }
}
