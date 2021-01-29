package com.libseat.admin.mapper;

import com.libseat.api.entity.RoleEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RoleMapper extends MyBaseMapper<RoleEntity> {

    List<RoleEntity> getRoleListBatch(@Param("ids") List<Integer> ids);
}
