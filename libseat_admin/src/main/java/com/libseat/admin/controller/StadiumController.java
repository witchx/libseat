package com.libseat.admin.controller;


import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.StadiumService;
import com.libseat.api.constant.HiddenType;
import com.libseat.api.entity.StadiumEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@CrossOrigin
@RequestMapping("/api/stadium")
@Controller
public class StadiumController {

    @Autowired
    private StadiumService stadiumService;

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<StadiumEntity>> getStadiumList (@RequestParam(required = false) Integer userId,
                                                                   @RequestParam(required = false) String name,
                                                                   @RequestParam(required = false) String companyName,
                                                                   @RequestParam(required = false) String address,
                                                                   @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<StadiumEntity> stadiumList = stadiumService.getStadiumList(userId, name, companyName, address, page, pageSize);
        if (stadiumList == null || stadiumList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(stadiumList);
        }
    }


    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateStadium (@PathVariable Integer id, @RequestBody StadiumEntity stadiumEntity) {
        if (stadiumEntity != null) {
            stadiumEntity.setId(id);
            String address = stadiumEntity.getAddress();
            if (StringUtils.isNotBlank(address)) {
                stadiumEntity.setAddress(address.trim());
            }
            String name = stadiumEntity.getName();
            if (StringUtils.isNotBlank(name)) {
                stadiumEntity.setName(name.trim());
            }
            String detailAddress = stadiumEntity.getDetailAddress();
            if (StringUtils.isNotBlank(detailAddress)) {
                stadiumEntity.setDetailAddress(detailAddress.trim());
            }
            stadiumEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (stadiumService.updateStadium(stadiumEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createStadium (@RequestBody StadiumEntity stadiumEntity) {
        if (stadiumEntity.getId() == null) {
            stadiumEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            stadiumEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            String address = stadiumEntity.getAddress();
            stadiumEntity.setAddress(address.trim());
            String name = stadiumEntity.getName();
            stadiumEntity.setName(name.trim());
            String detailAddress = stadiumEntity.getDetailAddress();
            stadiumEntity.setDetailAddress(detailAddress.trim());
            if (stadiumService.insertStadium(stadiumEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> createStadium (@PathVariable Integer id) {
        //先去查一遍，万一有用户在自习室预约,或者正在自习
        //TODO
        StadiumEntity stadiumEntity = new StadiumEntity();
        stadiumEntity.setId(id);
        if (stadiumService.deleteStadium(stadiumEntity) != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
