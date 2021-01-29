package com.libseat.admin.service;


import com.libseat.api.entity.StadiumEntity;
import com.libseat.utils.page.PageResult;

public interface StadiumService {
    PageResult<StadiumEntity> getStadiumList(Integer universityId, String name, String companyName, String address, Integer page, Integer pageSize);

    StadiumEntity getStadium(StadiumEntity stadiumEntity);

    Integer updateStadium(StadiumEntity stadiumEntity);

    Integer insertStadium(StadiumEntity stadiumEntity);

    Integer deleteStadium(StadiumEntity stadiumEntity);
}
