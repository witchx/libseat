package com.libseat.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.UserDetailMapper;
import com.libseat.admin.mapper.UserMapper;
import com.libseat.admin.service.UserService;
import com.libseat.api.entity.UserDetailEntity;
import com.libseat.api.entity.UserEntity;
import com.libseat.utils.page.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Override
    public PageResult<UserEntity> getUserList(Integer id, String username, String companyName, Integer status, Timestamp createTimeStart, Timestamp createTimeEnd, Timestamp lastLoginTimeStart, Timestamp lastLoginTimeEnd, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<UserEntity> userEntities = userMapper.selectUser(id, username, companyName, status ,createTimeStart, createTimeEnd, lastLoginTimeStart, lastLoginTimeEnd);
        PageInfo pageInfo = new PageInfo(userEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public Integer updateUser(UserEntity userEntity) {
        return userMapper.updateByPrimaryKeySelective(userEntity);
    }

    @Override
    public Integer insertUser(UserEntity userEntity) {
        return userMapper.insert(userEntity);
    }

    @Override
    public UserDetailEntity getUserDetail(UserDetailEntity userDetailEntity) {
        return userDetailMapper.selectOne(userDetailEntity);
    }

    @Override
    public UserEntity getUserById(UserEntity userEntity ) {
        return userMapper.selectByPrimaryKey(userEntity);
    }

    @Override
    public List<String> getAllUsername() {
        Example example = new Example(UserEntity.class);
        example.selectProperties("username");
        List<String> allUsername = userMapper.selectByExample(example).stream().map(userEntity -> userEntity.getUsername()).collect(Collectors.toList());
        return allUsername;
    }
}
