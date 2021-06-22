package com.libseat.server.web.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.libseat.api.constant.*;
import com.libseat.api.entity.*;
import com.libseat.server.framework.config.AlipayConfig;
import com.libseat.server.web.dto.PayDto;
import com.libseat.server.web.dto.PayInfo;
import com.libseat.server.web.mapper.PayMapper;
import com.libseat.server.web.service.*;
import com.libseat.utils.utils.CodeGenerateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
public class PayServiceImpl implements PayService {
    @Autowired
    private PayMapper payMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSeatService orderSeatService;

    @Autowired
    private CustomerBagService customerBagService;

    @Autowired
    private VipCardService vipCardService;

    @Autowired
    private RankService rankService;

    @Autowired
    private CustomerVipCardService customerVipCardService;

    @Autowired
    private CodeGenerateUtils codeGenerateUtils;

    //和支付宝签约的产品码 固定值
    private static final String PRODUCT_CODE = "QUICK_WAP_WAY";

    //支付成功标识(可退款的签约是TRADE_SUCCESS，不可退款的签约是TRADE_FINISHED)
    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    private final Object lock = new Object();

    private Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean createPay(PayDto payDto){
        synchronized (lock) {
            PaymentType paymentType = PaymentType.getById(payDto.getPaymentType());
            if (paymentType != null) {
                OrderType orderType = OrderType.getById(payDto.getOrderType());
                OrderEntity orderEntity = new OrderEntity();

                CustomerBagEntity customerBagEntity = customerBagService.getCustomerBagByCustomerId(payDto.getCustomerId());
                CustomerBagEntity customerBag = new CustomerBagEntity();
                customerBag.setId(customerBagEntity.getId());

                assert orderType != null;
                switch (orderType) {
                    case SEAT:
                        orderEntity.setStatus(OrderStatusType.CONFIRM.getId());
                        //只能是会员卡支付
                        //更新背包的会员卡
                        switch (paymentType) {
                            case VALUE:
                                if (customerBagEntity.getTotalValue().compareTo(BigDecimal.ZERO) < 1 ){
                                    return false;
                                }
                                OrderEntity orderEntity1 = new OrderEntity();
                                orderEntity1.setId(payDto.getOrderId());
                                OrderEntity order = orderService.getOrder(orderEntity1);
                                customerBag.setTotalValue(customerBagEntity.getTotalValue().subtract(order.getPrice()));
                                customerBagService.updateCustomerBag(customerBag);
                                break;
                            case WOULD:
                                if (customerBagEntity.getTotalTimes() <= 0 ){
                                    return false;
                                }
                                customerBag.setTotalTimes(customerBagEntity.getTotalTimes() - 1);
                                customerBagService.updateCustomerBag(customerBag);
                                break;
                            case TIME:
                                if (customerBagEntity.getTotalDays() <= 0 ){
                                    return false;
                                }
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                //更新支付表
                PayEntity payEntity = new PayEntity();
                payEntity.setUserId(payDto.getUserId());
                payEntity.setCustomerId(payDto.getCustomerId());
                payEntity.setPaymentType(payDto.getPaymentType());
                payEntity.setNo(codeGenerateUtils.generatePayCode());
                payEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                insertPay(payEntity);
                //更新基本订单表
                orderEntity.setId(payDto.getOrderId());
                orderEntity.setPayId(payEntity.getId());
                orderEntity.setCouponId(payDto.getCouponId());
                orderEntity.setProgress(OrderProgressType.PAY.getId());
                orderService.updateOrder(orderEntity);
            }
        }
        return true;
    }

    @Override
    public Integer insertPay(PayEntity payEntity) {
        return payMapper.insertUseGeneratedKeys(payEntity);
    }

    @Override
    public PayEntity getPayById(Integer id) {
        PayEntity payEntity = new PayEntity();
        payEntity.setId(id);
        return payMapper.selectByPrimaryKey(payEntity);
    }

    @Override
    public PayInfo getPayByOrderId(Integer id) {
        return payMapper.getPayByOrderId(id);
    }

    @Override
    public Object alipay(PayDto payDto){
        synchronized (lock) {
            String result = null;
            PaymentType paymentType = PaymentType.getById(payDto.getPaymentType());
            if (paymentType != null) {
                OrderType orderType = OrderType.getById(payDto.getOrderType());
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setId(payDto.getOrderId());
                OrderEntity order = orderService.getOrder(orderEntity);
                assert orderType != null;
                switch (orderType) {
                    case SEAT:
                        OrderSeatEntity orderSeatEntity = orderSeatService.getOrderByOrderId(payDto.getOrderId());
                        result = buildAliPayResult(order.getNo(),"座位["+orderSeatEntity.getSeatId()+"]",order.getPrice().toString(),"/" + order.getId());
                        break;
                    case VIP_CARD:
                        VipCardEntity vipCardEntity = vipCardService.getVipCardByOrderId(payDto.getOrderId());
                        result = buildAliPayResult(order.getNo(),vipCardEntity.getName(),order.getPrice().toString(),"/" + order.getId());
                        break;
                    default:
                        break;
                }
            }
            return result;
        }
    }

    @Override
    public void fallback(String orderNo) {
        synchronized (lock) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setNo(orderNo);
            OrderEntity order = orderService.getOrderSelective(orderEntity);
            OrderType orderType = OrderType.getById(order.getType());

            assert orderType != null;
            switch (orderType) {
                case SEAT:
                    orderEntity.setStatus(OrderStatusType.CONFIRM.getId());
                    break;
                case VIP_CARD:
                    //更新背包的会员卡
                    orderEntity.setStatus(OrderStatusType.COMPLETED.getId());
                    CustomerBagEntity customerBagEntity = customerBagService.getCustomerBagByCustomerId(order.getCustomerId());
                    CustomerBagEntity customerBag = new CustomerBagEntity();
                    customerBag.setId(customerBagEntity.getId());
                    VipCardEntity vipCardEntity = vipCardService.getVipCardByOrderId(order.getId());
                    Integer vipCardType = vipCardEntity.getType();
                    if (vipCardType.equals(VipCardType.VALUE_CARD.getId())) {
                        customerBag.setTotalValue(customerBagEntity.getTotalValue().add(vipCardEntity.getMoney()));
                    } else if(vipCardType.equals(VipCardType.WOULD_CARD.getId())){
                        customerBag.setTotalTimes(customerBagEntity.getTotalTimes()+vipCardEntity.getTimes());
                    } else if(vipCardType.equals(VipCardType.TIME_CARD.getId())){
                        customerBag.setTotalDays(customerBagEntity.getTotalDays()+vipCardEntity.getUsefulLife());
                    }
                    customerBagService.updateCustomerBag(customerBag);
                    //更新顾客的VIP卡
                    CustomerVipCardEntity customerVipCardEntity = new CustomerVipCardEntity();
                    customerVipCardEntity.setCustomerId(order.getCustomerId());
                    customerVipCardEntity.setVipCardId(vipCardEntity.getId());
                    customerVipCardEntity.setType(vipCardType);
                    customerVipCardService.insertCustomerVipCard(customerVipCardEntity);
                    break;
                default:
                    break;
            }
            //更新支付表
            PayEntity payEntity = new PayEntity();
            payEntity.setUserId(order.getUserId());
            payEntity.setCustomerId(order.getCustomerId());
            payEntity.setPaymentType(PaymentType.MONEY.getId());
            payEntity.setNo(codeGenerateUtils.generatePayCode());
            payEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            insertPay(payEntity);
            //更新基本订单表
            orderEntity.setId(order.getId());
            orderEntity.setPayId(payEntity.getId());
            orderEntity.setProgress(OrderProgressType.PAY.getId());
            orderService.updateOrder(orderEntity);
        }
    }

    @Override
    public void refund(String orderNo,String price) {
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APP_ID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGN_TYPE);
        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model=new AlipayTradeRefundModel();
        //退款的订单Id，也可以设置流水号
        model.setOutTradeNo(orderNo);
        //退款金额
        model.setRefundAmount(price);
        alipay_request.setBizModel(model);
        String alipay_response = "";
        try {
            alipay_response = client.execute(alipay_request).getBody();
        } catch (AlipayApiException e) {
            logger.info("【支付宝支付】退款失败 error={}", e);
        }
    }

    public String buildAliPayResult(String orderNo,String orderName,String price,String suffix) {


        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APP_ID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGN_TYPE);
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
        // 封装请求支付信息
        AlipayTradeWapPayModel model= new AlipayTradeWapPayModel();
        //订单编号，不可重复
        model.setOutTradeNo(orderNo);
        //订单名称
        model.setSubject(orderName);
        //订单金额
        model.setTotalAmount(price);
        //产品码
        model.setProductCode(PRODUCT_CODE);

        alipay_request.setBizModel(model);
        //支付成功后跳转的地址
        alipay_request.setReturnUrl(AlipayConfig.RETURN_URL);
        //异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
        // form表单生产
        String result = "";
        try {
            // 调用SDK生成表单
            result = client.pageExecute(alipay_request).getBody();
        } catch (AlipayApiException e) {
            logger.error("【支付宝支付】支付失败 error={}", e);
        }
        return result;
    }
}
