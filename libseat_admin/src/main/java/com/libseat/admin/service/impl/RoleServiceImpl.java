package com.libseat.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.RoleMapper;
import com.libseat.admin.service.RoleService;
import com.libseat.api.entity.RoleEntity;
import com.libseat.utils.page.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public PageResult<RoleEntity> getRoleList(String name, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Example example = new Example(RoleEntity.class);
        example.selectProperties("id","name","des","createTime","modifyTime","deleteFlag");
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        example.setOrderByClause("id");
        List<RoleEntity> roleEntities = roleMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(roleEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public List<RoleEntity> getRoleListBatch(List<Integer> ids) {
        return roleMapper.getRoleListBatch(ids);
    }

    @Override
    public Integer updateRole(RoleEntity roleEntity) {
        return roleMapper.updateByPrimaryKeySelective(roleEntity);
    }

    @Override
    public Integer insertRole(RoleEntity roleEntity) {
        return roleMapper.insertUseGeneratedKeys(roleEntity);
    }

    @Override
    public void deleteRole(RoleEntity roleEntity) {
        roleMapper.deleteByPrimaryKey(roleEntity);
    }
}
