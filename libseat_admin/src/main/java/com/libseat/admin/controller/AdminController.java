package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.AdminService;
import com.libseat.api.entity.AdminEntity;
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
@RequestMapping("/api/admin")
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<AdminEntity>> getAdminList (@RequestParam(required = false) String name,
                                                               @RequestParam(required = false, defaultValue = "1") Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<AdminEntity> manangerUserEntities = adminService.getAdminList(name, page, pageSize);
        if (manangerUserEntities == null || manangerUserEntities.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(manangerUserEntities);
        }

    }

    @RequestMapping(value = "/update/{id}" , method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> updateAdmin (@PathVariable Integer id, @RequestBody AdminEntity adminEntity){
        if (adminEntity != null) {
            adminEntity.setId(id);
            adminEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (adminService.updateAdmin(adminEntity) != 0) {
               return CommonResult.success();
           }
       }
       return CommonResult.failed();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createAdmin (@RequestBody AdminEntity adminEntity){
        if (adminEntity != null) {
            AdminEntity admin = adminService.getAdmin(adminEntity);
            if (admin != null && admin.getDeleteFlag().equals("0")) {
                return CommonResult.failed("已存在同名用户！");
            } else {
                adminEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                adminEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
                if (adminService.createAdmin(adminEntity) != 0) {
                    return CommonResult.success();
                }
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}" , method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ResultCode> deleteAdmin (@PathVariable Integer id){
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setId(id);
        adminService.deleteAdmin(adminEntity);
        return CommonResult.success();
    }
}
