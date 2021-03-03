package com.libseat.admin.service;


import com.libseat.api.entity.UserDetailEntity;

import java.util.List;

public interface UserDetailService {
    void statistics();

    Integer insertUserDetail(UserDetailEntity userDetailEntity);

    UserDetailEntity getUserDetailById(UserDetailEntity userDetailEntity);

    List<UserDetailEntity> getUserDetailList();

    UserDetailEntity getUserDetailByUserId(Integer id);
}
