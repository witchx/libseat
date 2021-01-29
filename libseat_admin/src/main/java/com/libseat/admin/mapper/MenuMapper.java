package com.libseat.admin.mapper;

import com.libseat.api.entity.MenuEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MenuMapper extends MyBaseMapper<MenuEntity> {
    Integer updateMenuBatch(@Param("menuEntities") List<MenuEntity> menuEntities);
}
