package com.libseat.admin.service;


import com.libseat.api.entity.AdminEntity;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface AdminService {
    AdminEntity login(AdminEntity adminEntity);

    PageResult<AdminEntity> getAdminList(String username, Integer page, Integer pageSize);

    AdminEntity getAdmin(AdminEntity adminEntity);

    Integer updateAdmin(AdminEntity adminEntity);

    Integer createAdmin(AdminEntity adminEntity);

    void addAdminToken(String token, String loginId) throws Exception;

    String getAdminToken( String loginId) throws Exception;

    void logout(String loginId) throws Exception;

    void deleteAdmin(AdminEntity adminEntity);

    List<String> getAllUsername();

    AdminEntity getAdminById(Integer id);
}
