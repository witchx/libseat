package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderProductMapper;
import com.libseat.admin.service.OrderProductService;
import com.libseat.admin.service.ProductService;
import com.libseat.api.entity.OrderProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private ProductService productService;

    @Override
    public List<OrderProductEntity> getOrderByOrderId(Integer orderId) {
        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setOrderId(orderId);
        List<OrderProductEntity> orderProduct = orderProductMapper.select(orderProductEntity);
        orderProduct.forEach(product -> product.setProduct(productService.getProductById(product.getProductId())));
        return orderProduct;
    }

    @Override
    public Integer updateOrderById(OrderProductEntity orderProductEntity) {
        return orderProductMapper.updateByPrimaryKeySelective(orderProductEntity);
    }
}
