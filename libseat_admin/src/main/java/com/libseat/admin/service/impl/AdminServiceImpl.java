package com.libseat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.AdminMapper;
import com.libseat.admin.service.AdminService;
import com.libseat.admin.service.RoleService;
import com.libseat.api.entity.AdminEntity;
import com.libseat.api.entity.RoleEntity;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.redis.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleService roleService;

    @Override
    public AdminEntity login(AdminEntity adminEntity) {

        AdminEntity manangerUser = null;
        Jedis jedis = null;
        adminEntity.setPassword(DigestUtils.md5Hex(adminEntity.getPassword()));
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null) {
                String key = "admin:" + adminEntity.getUsername() + adminEntity.getPassword() + adminEntity.getRole() + ":info";
                String manangerUserStr = jedis.get(key);
                if (StringUtils.isNotBlank(manangerUserStr)) {
                    //用户和密码正确
                    manangerUser = JSON.parseObject(manangerUserStr, AdminEntity.class);
                }
            }
            if (manangerUser == null) {
                //缓存连接异常，去数据库中查找
                //缓存中没有，去数据库中查找
                manangerUser = loginFromDb(adminEntity);
                if (manangerUser != null) {
                    //放入缓存中
                    String key = "admin:" + manangerUser.getUsername() + manangerUser.getPassword() + adminEntity.getRole() + ":info";
                    jedis.setex(key, 60 * 60 * 24, JSON.toJSONString(manangerUser));
                }
            }
            if (manangerUser != null){
                AdminEntity admin = new AdminEntity();
                admin.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
                updateAdmin(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return manangerUser;
    }

    private AdminEntity loginFromDb(AdminEntity adminEntity) {
        AdminEntity adminEntity1 = adminMapper.selectOne(adminEntity);
        return adminEntity1;
    }
    @Override
    public PageResult<AdminEntity> getAdminList(String username, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminEntity> adminEntities = adminMapper.getAdminList(username);
        adminEntities.forEach(adminEntity -> {
            ArrayList<Integer> arrayList = JSON.parseObject(adminEntity.getRole(), ArrayList.class);
            if (arrayList!=null&&!arrayList.isEmpty()) {
                List<RoleEntity> roleList = roleService.getRoleListBatch(arrayList);
                StringBuilder stringBuilder = new StringBuilder();
                for (RoleEntity roleEntity : roleList) {
                    stringBuilder.append(roleEntity.getName()).append(" ");
                }
                adminEntity.setRoleName(stringBuilder.toString());
            }
        });
        PageInfo pageInfo = new PageInfo(adminEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public AdminEntity getAdmin(AdminEntity adminEntity) {
        return adminMapper.selectOne(adminEntity);
    }



    @Override
    public Integer updateAdmin(AdminEntity adminEntity) {
        if (StringUtils.isNotBlank(adminEntity.getPassword())) {
            adminEntity.setPassword(DigestUtils.md5Hex(adminEntity.getPassword()));
        }
        return adminMapper.updateByPrimaryKeySelective(adminEntity);
    }

    @Override
    public Integer createAdmin(AdminEntity adminEntity) {
        adminEntity.setPassword(DigestUtils.md5Hex(adminEntity.getPassword()));
        return adminMapper.insert(adminEntity);
    }

    @Override
    public void addAdminToken(String token, String loginId) throws Exception{
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            jedis.setex("admin:" + loginId + ":token", 60 * 60 * 2, token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("redis 添加token失败");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String getAdminToken(String loginId) throws Exception {
        Jedis jedis = null;
        String token;
        try {
            jedis = redisUtil.getJedis();
            token = jedis.get("admin:" + loginId + ":token");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("redis 取得token失败");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return token;
    }

    @Override
    public void logout(String loginId) throws Exception {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String key = "admin:" + loginId + ":token";
            if (jedis.exists(key)) {
                jedis.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("redis 删除token失败");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void deleteAdmin(AdminEntity adminEntity) {
        adminMapper.deleteByPrimaryKey(adminEntity);
    }

    @Override
    public List<String> getAllUsername() {
        Example example = new Example(AdminEntity.class);
        example.selectProperties("username");
        List<String> allUsername = adminMapper.selectByExample(example).stream().map(adminEntity -> adminEntity.getUsername()).collect(Collectors.toList());
        return allUsername;
    }

    @Override
    public AdminEntity getAdminById(Integer id) {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setId(id);
        return adminMapper.selectByPrimaryKey(adminEntity);
    }
}
