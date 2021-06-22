package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.UserDetailMapper;
import com.libseat.admin.service.CustomerService;
import com.libseat.admin.service.OrderService;
import com.libseat.admin.service.StadiumService;
import com.libseat.admin.service.UserDetailService;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.api.entity.OrderEntity;
import com.libseat.api.entity.StadiumEntity;
import com.libseat.api.entity.UserDetailEntity;
import com.libseat.utils.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailService {
    @Autowired
    private UserDetailMapper userDetailMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private StadiumService stadiumService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void statistics() {
        //先得到所有的用户详情
        List<UserDetailEntity> userDetailEntities = getUserDetailList();
        long dayStartTime = DateUtils.getDayStartTime(System.currentTimeMillis());
        Timestamp yesEndTime = new Timestamp(dayStartTime);
        Timestamp yesStartTime = new Timestamp(dayStartTime - DateUtils.DAY);
        List<UserDetailEntity> userDetailEntityList = new LinkedList<>();
        userDetailEntities.forEach(userDetailEntity -> {
            Integer userId = userDetailEntity.getUserId();
            List<OrderEntity> orderEntities = orderService.getYesterdayOrderByUserId(userId,yesStartTime,yesEndTime);
            BigDecimal yesTurnover = BigDecimal.ZERO;
            orderEntities.forEach(orderEntity -> yesTurnover.add(orderEntity.getPrice()));
            UserDetailEntity userDetail = new UserDetailEntity();
            userDetail.setId(userDetailEntity.getId());
            userDetail.setYesOrderNum(orderEntities.size());
            userDetail.setOrderNum(userDetailEntity.getOrderNum() + userDetail.getYesOrderNum());
            userDetail.setYesTurnover(yesTurnover);
            userDetail.setTurnover(userDetailEntity.getTurnover().add(userDetail.getYesTurnover()));
            List<CustomerEntity> customerEntities = customerService.getYesterdayCustomerByUserId(userId,yesStartTime,yesEndTime);
            userDetail.setYesCustomerNum(customerEntities.size());
            userDetail.setCustomerNum(userDetailEntity.getCustomerNum() + userDetail.getYesCustomerNum());
            List<CustomerEntity> vipCustomerEntities = customerEntities.stream().filter(customerEntity -> customerEntity.getTotalDays() > 0 && customerEntity.getTotalTimes() > 0 && customerEntity.getTotalValue().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            userDetail.setYesVipCustomerNum(vipCustomerEntities.size());
            userDetail.setVipCustomerNum(userDetailEntity.getVipCustomerNum() + userDetail.getYesVipCustomerNum());
            List<StadiumEntity> stadiumEntities = stadiumService.getStadiumByUserId(userId);
            userDetail.setStadiumNum(stadiumEntities.size());
            userDetailEntityList.add(userDetail);
        });
        userDetailMapper.updateUserDetailBatch(userDetailEntityList);
    }

    @Override
    public Integer insertUserDetail(UserDetailEntity userDetailEntity) {
        return userDetailMapper.insert(userDetailEntity);
    }

    @Override
    public UserDetailEntity getUserDetailById(UserDetailEntity userDetailEntity) {
        return userDetailMapper.selectByPrimaryKey(userDetailEntity);
    }

    @Override
    public List<UserDetailEntity> getUserDetailList() {
        return userDetailMapper.selectAll();
    }

    @Override
    public UserDetailEntity getUserDetailByUserId(Integer id) {
        UserDetailEntity userDetailEntity = new UserDetailEntity();
        userDetailEntity.setUserId(id);
        return userDetailMapper.selectOne(userDetailEntity);
    }
}
