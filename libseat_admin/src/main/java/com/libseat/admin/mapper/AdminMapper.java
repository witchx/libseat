package com.libseat.admin.mapper;

import com.libseat.api.entity.AdminEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AdminMapper extends MyBaseMapper<AdminEntity> {
    List<AdminEntity> getAdminList(@Param("name") String name);
}
