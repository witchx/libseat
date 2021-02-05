package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.VipCardService;
import com.libseat.api.constant.VipCardType;
import com.libseat.api.entity.RoomEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.utils.CodeGenerateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author witch
 * 2020/11/10 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/vip")
@Controller
public class VipCardController {

    @Autowired
    private VipCardService vipCardService;
    @Autowired
    private CodeGenerateUtils codeGenerateUtils;

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<VipCardEntity>> getVipCardList (@RequestParam(required = false) Integer id,
                                                                  @RequestParam(required = false) String no,
                                                                  @RequestParam(required = false) String name,
                                                                  @RequestParam(required = false) Integer type,
                                                                  @RequestParam(required = false) String companyName,
                                                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<VipCardEntity> vipCardList = vipCardService.getVipCardList(id, no, name, type, companyName, page, pageSize);
        if (vipCardList == null || vipCardList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(vipCardList);
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createVipCard(@RequestBody VipCardEntity vipCardEntity) {
        vipCardEntity.setNo(codeGenerateUtils.generateProductCode(VipCardType.getById(vipCardEntity.getType()).getName()));
        vipCardEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        vipCardEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
        Integer row = vipCardService.insertVipCard(vipCardEntity);
        if (row != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateVipCard(@PathVariable Integer id, @RequestBody VipCardEntity vipCardEntity) {
        if (vipCardEntity != null) {
            vipCardEntity.setId(id);
            vipCardEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (vipCardService.updateVipCard(vipCardEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteVipCardBatch(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return CommonResult.failed();
        }
        vipCardService.deleteVipCardBatch(ids);
        return CommonResult.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteVipCard (@PathVariable Integer id){
        VipCardEntity vipCardEntity = new VipCardEntity();
        vipCardEntity.setId(id);
        vipCardService.deleteVipCard(vipCardEntity);
        return CommonResult.failed();
    }
}
