package com.libseat.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.MenuMapper;
import com.libseat.admin.mapper.OrderMapper;
import com.libseat.admin.service.MenuService;
import com.libseat.admin.service.OrderService;
import com.libseat.api.entity.MenuEntity;
import com.libseat.api.entity.OrderEntity;
import com.libseat.api.entity.RoleEntity;
import com.libseat.utils.page.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public PageResult<MenuEntity> getMenuList(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<MenuEntity> menuEntities = getMenuList(false);
        PageInfo pageInfo = new PageInfo(menuEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public List<MenuEntity> getMenuList(Boolean isHidden) {
        Example example = new Example(MenuEntity.class);
        if (isHidden) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("hidden","0");
        }
        return menuMapper.selectByExample(example);
    }

    @Override
    public MenuEntity getMenu(MenuEntity menuEntity) {
        return null;
    }

    @Override
    public Integer updateMenu(MenuEntity menuEntity) {
        return menuMapper.updateByPrimaryKeySelective(menuEntity);
    }

    @Override
    public Integer updateMenuBatch(List<MenuEntity> menuEntities) {
        return menuMapper.updateMenuBatch(menuEntities);
    }

    @Override
    public Integer insertMenu(MenuEntity menuEntity) {
        return menuMapper.insertUseGeneratedKeys(menuEntity);
    }

    @Override
    public void deleteMenu(MenuEntity menuEntity) {
        menuMapper.deleteByPrimaryKey(menuEntity);
    }
}
