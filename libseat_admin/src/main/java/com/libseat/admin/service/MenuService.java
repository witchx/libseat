package com.libseat.admin.service;


import com.libseat.api.entity.MenuEntity;
import com.libseat.api.entity.OrderEntity;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface MenuService {
    PageResult<MenuEntity> getMenuList(Integer page, Integer pageSize);

    List<MenuEntity> getMenuList(Boolean isHidden);

    MenuEntity getMenu(MenuEntity menuEntity);

    Integer updateMenu(MenuEntity menuEntity);

    Integer updateMenuBatch(List<MenuEntity> menuEntities);

    Integer insertMenu(MenuEntity menuEntity);

    void deleteMenu(MenuEntity menuEntity);

}
