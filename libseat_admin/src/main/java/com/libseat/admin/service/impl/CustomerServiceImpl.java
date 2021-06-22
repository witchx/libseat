package com.libseat.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.CustomerMapper;
import com.libseat.admin.service.CustomerBagService;
import com.libseat.admin.service.CustomerService;
import com.libseat.admin.service.RankService;
import com.libseat.api.entity.CustomerBagEntity;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.api.entity.RankEntity;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerBagService customerBagService;
    @Autowired
    private RankService rankService;

    @Override
    public PageResult<CustomerEntity> getCustomerList(String username, Integer sex,  Integer userId, Timestamp createTimeStart, Timestamp createTimeEnd, Timestamp lastLoginTimeStart, Timestamp lastLoginTimeEnd, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CustomerEntity> CustomerEntities = customerMapper.getCustomerList(username, sex, userId, createTimeStart,createTimeEnd,lastLoginTimeStart,lastLoginTimeEnd);
        PageInfo pageInfo = new PageInfo(CustomerEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public Integer insertCustomerBatch(List<CustomerEntity> customerEntities) {
        return customerMapper.insertCustomerBatch(customerEntities);
    }

    @Override
    public void deleteCustomerBatch(List<CustomerEntity> customerEntities) {
        customerMapper.deleteCustomerBatch(customerEntities);
    }

    @Override
    public Integer updateCustomer(CustomerEntity customerEntity) {
        return customerMapper.updateByPrimaryKeySelective(customerEntity);
    }

    @Override
    public CustomerEntity getCustomerById(Integer customerId) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        return customerMapper.selectByPrimaryKey(customerEntity);
    }

    @Override
    public List<CustomerEntity> getYesterdayCustomerByUserId(Integer userId, Timestamp yesStartTime, Timestamp yesEndTime) {
        return customerMapper.getYesterdayCustomerByUserId(userId,yesStartTime,yesEndTime);
    }

    @Override
    public List<String> getAllUsername() {
        Example example = new Example(CustomerEntity.class);
        example.selectProperties("username");
        List<String> allUsername = customerMapper.selectByExample(example).stream().map(customerEntity -> customerEntity.getUsername()).collect(Collectors.toList());
        return allUsername;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insertCustomer(CustomerEntity customerEntity) {
        Integer i = customerMapper.insertSelective(customerEntity);
        CustomerBagEntity customerBagEntity = new CustomerBagEntity();
        customerBagEntity.init();
        customerBagEntity.setCustomerId(customerEntity.getId());
        Integer j = customerBagService.insertCustomerBag(customerBagEntity);
        RankEntity rankEntity = new RankEntity();
        rankEntity.init();
        rankEntity.setCustomerId(customerEntity.getId());
        rankEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
        Integer k = rankService.insertRank(rankEntity);
        return i+j+k;
    }

    @Override
    public CustomerBagEntity getCustomerBagByCustomerId(Integer id) {
        CustomerBagEntity customerBagEntity = customerBagService.getCustomerBagByCustomerId(id);
       /* int flag = DateUtils.getWeeksBetweenToday(Constant.OPEN_DAY);
        customerBagEntity.setRankWeek(rankService.getRank(Constant.RANK_WEEK+flag,id));
        customerBagEntity.setRankMonth(rankService.getRank(Constant.RANK_MONTH+flag,id));
        customerBagEntity.setRankYear(rankService.getRank(Constant.RANK_YEAR+flag,id));*/
        return customerBagEntity;
    }

}
