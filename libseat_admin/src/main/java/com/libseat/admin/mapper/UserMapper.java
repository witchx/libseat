package com.libseat.admin.mapper;

import com.libseat.api.entity.UserEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Component
public interface UserMapper extends MyBaseMapper<UserEntity> {
    List<UserEntity> selectUser(@Param("id") Integer id,
                                @Param("username") String username,
                                @Param("companyName") String companyName,
                                @Param("status") Integer status,
                                @Param("createTimeStart") Timestamp createTimeStart,
                                @Param("createTimeEnd") Timestamp createTimeEnd,
                                @Param("lastLoginTimeStart") Timestamp lastLoginTimeStart,
                                @Param("lastLoginTimeEnd") Timestamp lastLoginTimeEnd);
}
