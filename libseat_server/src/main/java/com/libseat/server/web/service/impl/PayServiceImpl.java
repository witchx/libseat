package com.libseat.server.web.service.impl;

import com.libseat.api.constant.*;
import com.libseat.api.entity.*;
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

import static com.libseat.api.constant.PaymentType.MONEY;

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

    private final Object lock = new Object();

    private Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createPay(PayDto payDto) throws Exception {
        synchronized (lock) {
            PaymentType paymentType = PaymentType.getById(payDto.getPaymentType());
            if (paymentType != null) {
                //更新支付表
                PayEntity payEntity = new PayEntity();
                payEntity.setUserId(payDto.getUserId());
                payEntity.setCustomerId(payDto.getCustomerId());
                payEntity.setPaymentType(payDto.getPaymentType());
                payEntity.setNo(codeGenerateUtils.generatePayCode());
                payEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                insertPay(payEntity);
                //更新基本订单表
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setId(payDto.getOrderId());
                orderEntity.setPayId(payEntity.getId());
                orderEntity.setCouponId(payDto.getCouponId());
                orderEntity.setDiscount(new BigDecimal(payDto.getDiscount()));
                OrderType orderType = OrderType.getById(payDto.getOrderType());
                assert orderType != null;
                switch (orderType) {
                    case SEAT:
                        orderEntity.setProgress(OrderProgressType.PAY.getId());
                        orderEntity.setStatus(OrderStatusType.CONFIRM.getId());
                        orderService.updateOrder(orderEntity);
                        if (!paymentType.equals(MONEY)) {
                            //更新背包的会员卡
                            CustomerBagEntity customerBagEntity = customerBagService.getCustomerBagByCustomerId(payDto.getCustomerId());
                            CustomerBagEntity customerBag = new CustomerBagEntity();
                            customerBag.setId(customerBagEntity.getId());
                            switch (paymentType) {
                                case VALUE:
                                    OrderEntity orderEntity1 = new OrderEntity();
                                    orderEntity1.setId(payDto.getOrderId());
                                    OrderEntity order = orderService.getOrder(orderEntity1);
                                    customerBag.setTotalValue(customerBagEntity.getTotalValue().subtract(order.getPrice()));
                                    customerBagService.updateCustomerBag(customerBag);
                                    break;
                                case WOULD:
                                    customerBag.setTotalTimes(customerBagEntity.getTotalTimes() - 1);
                                    customerBagService.updateCustomerBag(customerBag);
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case VIP_CARD:
                        //更新背包的会员卡
                        orderEntity.setProgress(OrderProgressType.PAY.getId());
                        orderEntity.setStatus(OrderStatusType.COMPLETED.getId());
                        orderService.updateOrder(orderEntity);
                        VipCardEntity vipCardEntity = vipCardService.getVipCardByOrderId(orderEntity.getId());
                        if (vipCardEntity == null) {
                            throw new Exception("vip card is null");
                        }
                        Integer vipCardType = vipCardEntity.getType();
                        CustomerBagEntity customerBagEntity = customerBagService.getCustomerBagByCustomerId(payDto.getCustomerId());
                        CustomerBagEntity customerBag = new CustomerBagEntity();
                        customerBag.setId(customerBagEntity.getId());
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
                        customerVipCardEntity.setCustomerId(payDto.getCustomerId());
                        customerVipCardEntity.setVipCardId(vipCardEntity.getId());
                        customerVipCardEntity.setType(vipCardType);
                        customerVipCardService.insertCustomerVipCard(customerVipCardEntity);

                        break;
                    default:
                        break;
                }
            }
        }
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
}
