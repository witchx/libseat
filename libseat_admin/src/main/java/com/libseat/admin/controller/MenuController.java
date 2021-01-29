package com.libseat.admin.controller;

import com.alibaba.fastjson.JSON;
import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.MenuService;
import com.libseat.api.entity.MenuEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author witch
 * 2020/11/10 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/menu")
@Controller
public class MenuController {

    @Autowired
    private MenuService menuService;

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<MenuEntity>> getMenuList (@RequestParam(required = false, defaultValue = "1") Integer page,
                                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<MenuEntity> menuList = menuService.getMenuList(page, pageSize);
        if (menuList == null || menuList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(menuList);
        }
    }

    @RequestMapping(value = "/update/{id}" , method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> updateMenu (@PathVariable Integer id, @RequestBody MenuEntity menuEntity){
        if (menuEntity != null) {
            menuEntity.setId(id);
            menuEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (menuService.updateMenu(menuEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/updateBatch/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> updateMenuBatch(@PathVariable Integer roleId ,@RequestBody List<Integer> menus) {
        List<MenuEntity> collect = menuService.getMenuList(false);
        List<MenuEntity> menuEntities = collect.stream().filter(menuEntity -> {
            Boolean flag = false;
            ArrayList<Integer> arrayList = JSON.parseObject(menuEntity.getAccess(), ArrayList.class);
            for (Integer menu : menus) {
                if (menuEntity.getId().equals(menu)) {
                    if (arrayList.isEmpty() || !arrayList.contains(roleId)) {
                        arrayList.add(roleId);
                    }
                    flag = true;
                }
            }
            if (!flag && arrayList.contains(roleId)) {
                arrayList.remove(roleId);
            }
            menuEntity.setAccess(JSON.toJSONString(arrayList));
            return true;
        }).collect(Collectors.toList());
        Integer row = menuService.updateMenuBatch(menuEntities);
        if (row != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createMenu (@RequestBody MenuEntity menuEntity){
        if (menuEntity != null) {
            menuEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            menuEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (menuService.insertMenu(menuEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}" , method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ResultCode> deleteMenu (@PathVariable Integer id){
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setId(id);
        menuService.deleteMenu(menuEntity);
        return CommonResult.success();
    }
}
