package com.libseat.admin.mapper;

import com.libseat.api.entity.UserDetailEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserDetailMapper extends MyBaseMapper<UserDetailEntity> {
    void updateUserDetailBatch(@Param("userDetailEntityList") List<UserDetailEntity> userDetailEntityList);
}
