package com.libseat.server.web.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.entity.AdminEntity;
import com.libseat.api.entity.CustomerBagEntity;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.api.entity.RankEntity;
import com.libseat.server.web.dto.CompanyInfo;
import com.libseat.server.web.dto.CustomerInfo;
import com.libseat.server.web.mapper.CustomerMapper;
import com.libseat.server.web.mapper.UserMapper;
import com.libseat.server.web.service.CustomerBagService;
import com.libseat.server.web.service.MyService;
import com.libseat.server.web.service.RankService;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.redis.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MyServiceImpl implements MyService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerBagService customerBagService;

    @Autowired
    private RankService rankService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<CustomerEntity> getCustomerList(String name, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Example example = new Example(AdminEntity.class);
        example.selectProperties( "id", "sid");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag","0");
        if (StringUtils.isNotBlank(name)) {
            criteria.andLike("sid", "%" + name + "%");
        }
        example.setOrderByClause("id");
        List<CustomerEntity> customerEntities = customerMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(customerEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public CustomerEntity getCustomer(CustomerEntity customerEntity) {
        return customerMapper.selectOne(customerEntity);
    }

    @Override
    public CustomerInfo getCustomerDetail(CustomerEntity customerEntity) {
        return customerMapper.getCustomerDetail(customerEntity);
    }

    @Override
    public Integer updateCustomer(CustomerEntity customerEntity) {
        if (StringUtils.isNotBlank(customerEntity.getPassword())) {
            customerEntity.setPassword(DigestUtils.md5Hex(customerEntity.getPassword()));
        }
        return customerMapper.updateByPrimaryKeySelective(customerEntity);
    }

    @Override
    public Integer createCustomer(CustomerEntity customerEntity) {
        customerEntity.setPassword(DigestUtils.md5Hex(customerEntity.getPassword()));
        customerEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
        return customerMapper.insert(customerEntity);
    }

    @Override
    public CustomerEntity getCustomerInfo(CustomerEntity customerEntity) {
        return customerMapper.getCustomerInfo(customerEntity);
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
        rankEntity.setUserId(customerEntity.getUserId());
        rankEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
        Integer k = rankService.insertRank(rankEntity);
        return i+j+k;
    }

    @Override
    public List<CompanyInfo> getAllCompany() {
        return userMapper.getAllCompany();
    }

}
