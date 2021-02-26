package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.*;
import com.libseat.api.constant.*;
import com.libseat.api.entity.*;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author witch
 */
@CrossOrigin
@RequestMapping("/api/order")
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderSettingService orderSettingService;
    @Autowired
    private OrderVipService orderVipService;
    @Autowired
    private OrderSeatService orderSeatService;
    @Autowired
    private OrderRecordService orderRecordService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private StadiumService stadiumService;

    @TrimRequired
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<OrderEntity>> getOrderList (@RequestParam(required = false) Integer id,
                                                               @RequestParam(required = false) String no,
                                                               @RequestParam(required = false) String company,
                                                               @RequestParam(required = false) String customer,
                                                               @RequestParam(required = false) String createTimeStart,
                                                               @RequestParam(required = false) String createTimeEnd,
                                                               @RequestParam(required = false) Integer type,
                                                               @RequestParam(required = false) Integer progress,
                                                               @RequestParam(required = false) Integer status,
                                                               @RequestParam(required = false, defaultValue = "1") Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<OrderEntity> orderList = orderService.getOrderList(id, no, company, customer,
                DateUtils.strToTimestamp(createTimeStart,DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.strToTimestamp(createTimeEnd,DateUtils.YYYY_MM_DD_HH_MM_SS), type, progress, status, page, pageSize);
        if (orderList == null || orderList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(orderList);
        }
    }

    @TrimRequired
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Object> getOrderDetail (@RequestParam Integer id, @RequestParam Integer type){
        OrderType orderType = OrderType.getById(type);
        Order order = null;
        OrderEntity orderEntity = orderService.getOrderById(id);
        if (orderType != null) {
            switch (orderType) {
                case SEAT:
                    order = orderSeatService.getOrderByOrderId(id);
                    break;
                case VIP_CARD:
                    order = orderVipService.getOrderByOrderId(id);
                    break;
                default:
                    break;
            }
        }
        if (orderEntity != null) {
            if (order == null) {
                order = new Order();
            }
            //设置顾客信息
            order.setOrder(orderEntity);
            order.setCustomer(customerService.getCustomerById(orderEntity.getCustomerId()));
            //设置商家信息
            UserEntity userEntity = new UserEntity();
            userEntity.setId(orderEntity.getUserId());
            order.setUser(userService.getUserById(userEntity));
            //设置场馆信息
            StadiumEntity stadiumEntity = new StadiumEntity();
            stadiumEntity.setId(orderEntity.getStadiumId());
            StadiumEntity stadium = stadiumService.getStadium(stadiumEntity);
            order.setStadiumAddr(stadium.getAddress() + "-" + stadium.getDetailAddress() );
            return CommonResult.success(order);
        }
        return CommonResult.failed();
    }

    @TrimRequired
    @RequestMapping(value = "/product",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OrderProductEntity>> getOrderProduct (@RequestParam Integer id){
        List<OrderProductEntity> orderByOrderId = orderProductService.getOrderByOrderId(id);
        return CommonResult.success(orderByOrderId);
    }

    @TrimRequired
    @RequestMapping(value = "/record",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OrderRecordEntity>> getOrderRecord (@RequestParam Integer id){
        //设置操作记录信息
        OrderRecordEntity orderRecordEntity = new OrderRecordEntity();
        orderRecordEntity.setOrderId(id);
        List<OrderRecordEntity> orderRecords = orderRecordService.getOrderRecord(orderRecordEntity);
        orderRecords.forEach(orderRecord -> {
            orderRecord.setOrderStatus(OrderStatusType.getById(orderRecord.getOrderStatusType()).getDes());
            orderRecord.setOperation(OperationType.getById(orderRecord.getOperationType()).getDes());
        });
        orderRecords.stream().sorted(Comparator.comparing(OrderRecordEntity::getId)).collect(Collectors.toList());
        return CommonResult.success(orderRecords);
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

    @RequestMapping(value = "/createRecord", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createOrderRecord (@RequestBody OrderRecordEntity orderRecordEntity){
        if (orderRecordEntity != null) {
            //在管理后台操作者类型只可能为管理员
            orderRecordEntity.setOperationType(OperationType.ADMIN.getId());
            orderRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            if (orderRecordService.insertOrderRecord(orderRecordEntity) != 0) {
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

    @RequestMapping(value = "/setting/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OrderSettingEntity>> getOrderSettingList (){
        List<OrderSettingEntity> orderSettingList = orderSettingService.getOrderSettingList();
        if (orderSettingList == null || orderSettingList.isEmpty()) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            orderSettingList.forEach(orderSettingEntity ->
                    orderSettingEntity.setUnitName(UnitType.getById(orderSettingEntity.getUnit()).getDes()));
            return CommonResult.success(orderSettingList);
        }
    }

    @RequestMapping(value = "/setting/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateOrder (@PathVariable Integer id, @RequestBody OrderSettingEntity orderSettingEntity){
        if (orderSettingEntity != null) {
            orderSettingEntity.setId(id);
            orderSettingEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            //开启的情况才需要重启
            if (orderSettingService.updateOrderSetting(orderSettingEntity,orderSettingEntity.getOnOff()) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/setting/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createOrderSetting (@RequestBody OrderSettingEntity orderSettingEntity){
        if (orderSettingEntity != null) {
            orderSettingEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (orderSettingService.insertOrderSetting(orderSettingEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }
}
