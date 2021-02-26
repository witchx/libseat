package com.libseat.admin.service;


import com.libseat.api.entity.UserDetailEntity;
import com.libseat.api.entity.UserEntity;
import com.libseat.utils.page.PageResult;

import java.sql.Timestamp;
import java.util.List;

public interface UserService {
    PageResult<UserEntity> getUserList(Integer id, String username, String companyName, Integer status, Timestamp createTimeStart, Timestamp createTimeEnd, Timestamp lastLoginTimeStart, Timestamp lastLoginTimeEnd, Integer page, Integer pageSize);

    Integer updateUser(UserEntity userEntity);

    Integer insertUser(UserEntity userEntity);

    UserDetailEntity getUserDetail(UserDetailEntity userDetailEntity);

    UserEntity getUserById(UserEntity userEntity );

    List<String> getAllUsername();
}
