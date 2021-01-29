package com.libseat.admin.service;

import com.libseat.api.entity.RoleEntity;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface RoleService {

    PageResult<RoleEntity> getRoleList(String name, Integer page, Integer pageSize);

    List<RoleEntity> getRoleListBatch(List<Integer> ids);

    Integer updateRole(RoleEntity roleEntity);

    Integer insertRole(RoleEntity roleEntity);

    void deleteRole(RoleEntity roleEntity);
}
