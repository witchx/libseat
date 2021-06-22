package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.RoleService;
import com.libseat.api.entity.RoleEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

/**
 * @author witch
 * 2020/11/10 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/role")
@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<RoleEntity>> getRoleList (@RequestParam(required = false) String name,
                                                             @RequestParam(required = false, defaultValue = "1") Integer page,
                                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<RoleEntity> roleList = roleService.getRoleList(name, page, pageSize);
        if (roleList == null || roleList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(roleList);
        }
    }

    @RequestMapping(value = "/update/{id}" , method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> updateRole (@PathVariable Integer id, @RequestBody RoleEntity roleEntity){
        if (roleEntity != null) {
            roleEntity.setId(id);
            roleEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (roleService.updateRole(roleEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createRole (@RequestBody RoleEntity roleEntity){
        if (roleEntity != null) {
            roleEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            roleEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (roleService.insertRole(roleEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}" , method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ResultCode> deleteRole (@PathVariable Integer id){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        roleService.deleteRole(roleEntity);
        return CommonResult.success();
    }
}
