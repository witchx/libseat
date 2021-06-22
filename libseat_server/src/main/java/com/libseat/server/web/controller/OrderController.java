package com.libseat.server.web.controller;


import com.libseat.api.constant.OrderStatusType;
import com.libseat.api.constant.OrderType;
import com.libseat.api.entity.*;
import com.libseat.server.web.dto.OrderDto;
import com.libseat.server.web.dto.OrderInfo;
import com.libseat.server.web.dto.OrderRecordInfo;
import com.libseat.server.web.dto.SeatOrderInfo;
import com.libseat.server.web.service.*;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RequestMapping("/api/order")
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSeatService orderSeatService;

    @Autowired
    private VipCardService vipCardService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PayService payService;

    @GetMapping(value = "/seat/all")
    @ResponseBody
    public CommonResult<List<SeatOrderInfo>> getSeatOrderList (@RequestParam Integer id){
        if (id != null) {
            List<SeatOrderInfo> seatOrderInfos = orderService.getAllSeatOrderList(id);
            if (seatOrderInfos != null && !seatOrderInfos.isEmpty()) {
                return CommonResult.success(seatOrderInfos);
            }
        }
        return CommonResult.failed();
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public CommonResult<List<OrderRecordInfo>> getOrderList (@RequestParam Integer id){
        if (id != null) {
            List<OrderRecordInfo> orderRecordInfos = orderService.getAllOrderList(id);
            if (orderRecordInfos != null && !orderRecordInfos.isEmpty()) {
                return CommonResult.success(orderRecordInfos);
            }
        }
        return CommonResult.failed();
    }

    @PostMapping("/create")
    @ResponseBody
    public CommonResult<Integer> createOrder (@RequestBody OrderDto orderDto){
        return orderService.createOrder(orderDto);
    }

    @GetMapping("/info")
    @ResponseBody
    public CommonResult<OrderInfo> getOrder(@RequestParam Integer id) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(id);
        OrderEntity order = orderService.getOrder(orderEntity);
        if (order == null) {
            return CommonResult.failed();
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPrice(order.getPrice().toString());
        orderInfo.setDiscount(order.getDiscount().toString());
        orderInfo.setType(order.getType());
        switch (Objects.requireNonNull(OrderType.getById(order.getType()))){
            case SEAT:
                OrderSeatEntity orderSeatEntity = orderSeatService.getOrderByOrderId(order.getId());
                orderInfo.setStartTime(orderSeatEntity.getStartTime());
                orderInfo.setEndTime(orderSeatEntity.getEndTime());
                orderInfo.setSeatId(orderSeatEntity.getSeatId());
                break;
            case VIP_CARD:
                VipCardEntity vipCardEntity = vipCardService.getVipCardByOrderId(order.getId());
                orderInfo.setCardType(vipCardEntity.getType());
                orderInfo.setMoney(vipCardEntity.getMoney()!=null?vipCardEntity.getMoney().toString():null);
                orderInfo.setTimes(vipCardEntity.getTimes());
                orderInfo.setUsefulLife(vipCardEntity.getUsefulLife());
                break;
            default:
                break;
        }
        return CommonResult.success(orderInfo);
    }

    @GetMapping("/detail")
    @ResponseBody
    public CommonResult<OrderEntity> getOrderDetail(@RequestParam Integer id, @RequestParam Integer type) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(id);
        OrderEntity order = orderService.getOrder(orderEntity);
        if (order != null) {
            if (order.getPayId() != null){
                //可能未支付
                PayEntity payEntity = payService.getPayById(order.getPayId());
                order.setPaymentType(payEntity.getPaymentType());
            }
            switch (Objects.requireNonNull(OrderType.getById(type))) {
                case SEAT:
                    OrderSeatEntity orderSeatEntity = orderSeatService.getOrderByOrderId(id);
                    order.setOrderSeat(orderSeatEntity);
                    break;
                case VIP_CARD:
                    VipCardEntity vipCardEntity = vipCardService.getVipCardByOrderId(id);
                    order.setVipCard(vipCardEntity);
                    break;
                case PRODUCT:
                    OrderProductEntity orderProductEntity = orderProductService.getOrderByOrderId(id);
                    order.setOrderProduct(orderProductEntity);
                    ProductEntity productEntity = productService.getProductById(orderProductEntity.getProductId());
                    order.setProduct(productEntity);
                    break;
                default:
                    break;
            }
            return CommonResult.success(order);
        }
        return CommonResult.failed();
    }

    @PostMapping("/update_{id}")
    @ResponseBody
    public CommonResult<ResultCode> updateOrder (@PathVariable Integer id, @RequestBody OrderEntity orderEntity){
        if (id != null && orderEntity != null) {
            orderEntity.setId(id);
            if (orderEntity.getEvaluate() != null) {
                orderEntity.setEvaluateTime(new Timestamp(System.currentTimeMillis()));
            }
            if (orderEntity.getStatus().equals(OrderStatusType.CANCEL.getId()) && orderEntity.getType().equals(OrderType.SEAT.getId())) {
                OrderSeatEntity orderSeatEntity = orderSeatService.getOrderByOrderId(id);
                if ((System.currentTimeMillis()+ DateUtils.MINUTE*30) > orderSeatEntity.getStartTime().getTime()) {
                    //离开始时间不到30分钟
                    return CommonResult.failed("离开始时间不到30分钟,不能取消预约！");
                }else {
                   orderService.cancelOrder(orderEntity);
                    return CommonResult.success();
                }
            }else {
                orderService.updateOrder(orderEntity);
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }
}
