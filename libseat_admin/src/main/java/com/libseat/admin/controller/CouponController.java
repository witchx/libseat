package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.CouponService;
import com.libseat.api.constant.CouponType;
import com.libseat.api.entity.CouponEntity;
import com.libseat.api.entity.ProductEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.utils.CodeGenerateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author witch
 * 2020/11/10 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/coupon")
@Controller
public class CouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CodeGenerateUtils codeGenerateUtils;

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<CouponEntity>> getCouponList (@RequestParam(required = false) Integer id,
                                                               @RequestParam(required = false) String no,
                                                               @RequestParam(required = false) String name,
                                                               @RequestParam(required = false) Integer type,
                                                               @RequestParam(required = false) String companyName,
                                                               @RequestParam(required = false, defaultValue = "1") Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<CouponEntity> couponList = couponService.getCouponList(id, no, name, type, companyName, page, pageSize);
        if (couponList == null || couponList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(couponList);
        }
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createCoupon(@RequestBody CouponEntity couponEntity) {
        couponEntity.setNo(codeGenerateUtils.generateProductCode(CouponType.getById(couponEntity.getType()).getName()));
        if (couponService.insertCoupon(couponEntity) != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateCoupon(@PathVariable Integer id, @RequestBody CouponEntity couponEntity) {
        if (couponEntity != null) {
            couponEntity.setId(id);
            if (couponService.updateCoupon(couponEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteCouponBatch(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return CommonResult.failed();
        }
        couponService.deleteCouponBatch(ids);
        return CommonResult.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteCoupon (@PathVariable Integer id){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(id);
        couponService.deleteCoupon(couponEntity);
        return CommonResult.failed();
    }
}
