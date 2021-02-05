package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.OrderService;
import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.entity.OrderEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/api/order")
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @TrimRequired
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<OrderEntity>> getOrderList (@RequestParam(required = false) Integer id,
                                                               @RequestParam(required = false) String no,
                                                               @RequestParam(required = false) String company,
                                                               @RequestParam(required = false) String customer,
                                                               @RequestParam(required = false) String createTimeStart,
                                                               @RequestParam(required = false) String createTimeEnd,
                                                               @RequestParam(required = false) Integer type,
                                                               @RequestParam(required = false) Integer progress,
                                                               @RequestParam(required = false, defaultValue = "1") Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<OrderEntity> orderList = orderService.getOrderList(id, no, company, customer,
                DateUtils.strToTimestamp(createTimeStart,DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.strToTimestamp(createTimeEnd,DateUtils.YYYY_MM_DD_HH_MM_SS), type, progress, page, pageSize);
        if (orderList == null || orderList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(orderList);
        }
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateOrder (@PathVariable Integer id, @RequestBody OrderEntity orderEntity){
        if (orderEntity != null) {
            orderEntity.setId(id);
            if (orderService.updateOrder(orderEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteOrder (@PathVariable Integer id, @RequestBody OrderEntity orderEntity){
        if (orderEntity != null) {
            orderEntity.setId(id);
            orderEntity.setDeleteFlag(DeleteFlagType.CANCEL.getId());
            if (orderService.updateOrder(orderEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }
}
