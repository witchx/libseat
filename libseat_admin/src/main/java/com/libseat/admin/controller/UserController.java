package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.UserService;
import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.entity.UserDetailEntity;
import com.libseat.api.entity.UserEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.utils.DateUtils;
import com.libseat.utils.utils.UsernameGenerateUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/api/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private final Object lock = new Object();

    @TrimRequired
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<UserEntity>> getUserList (@RequestParam(required = false) Integer id,
                                                             @RequestParam(required = false) String username,
                                                             @RequestParam(required = false) String companyName,
                                                             @RequestParam(required = false) Integer status,
                                                             @RequestParam(required = false) String createStartTime,
                                                             @RequestParam(required = false) String createEndTime,
                                                             @RequestParam(required = false) String lastLoginTimeStart,
                                                             @RequestParam(required = false) String lastLoginTimeEnd,
                                                             @RequestParam(required = false, defaultValue = "1") Integer page,
                                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<UserEntity> userList = userService.getUserList(id, username, companyName, status,
                DateUtils.strToTimestamp(createStartTime,DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.strToTimestamp(createEndTime,DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.strToTimestamp(lastLoginTimeStart,DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.strToTimestamp(lastLoginTimeEnd,DateUtils.YYYY_MM_DD_HH_MM_SS),
                page, pageSize);
        if (userList == null || userList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(userList);
        }
    }

    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UserDetailEntity> getUserDetail (@PathVariable Integer id){
        UserDetailEntity userDetail = userService.getUserDetailByUserId(id);
        if (userDetail != null) {
            return CommonResult.success(userDetail);
        }
        return CommonResult.failed(ResultCode.EMPTY);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateUser (@PathVariable Integer id, @RequestBody UserEntity userEntity){
        if (userEntity != null) {
            userEntity.setId(id);
            userEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            String password = userEntity.getPassword();
            if (StringUtils.isNotBlank(password)) {
                userEntity.setPassword(DigestUtils.md5Hex(password));
            }
            if (userService.updateUser(userEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/password/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateUserPassword (@PathVariable Integer id,
                                                        @RequestBody Map<String,String> data){
        String password = data.get("password");
        String password2 = data.get("password2");
        if ( StringUtils.isBlank(password) || StringUtils.isBlank(password2)) {
            return CommonResult.failed();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        UserEntity user = userService.getUserById(userEntity);
        if (user.getPassword().equals(DigestUtils.md5Hex(password))) {
            userEntity.setPassword(DigestUtils.md5Hex(password2));
            userEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (userService.updateUser(userEntity) != 0) {
                return CommonResult.success();
            }
        } else {
            return CommonResult.failed("原密码错误！");
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createUser ( @RequestBody UserEntity userEntity){
        if (userEntity != null) {
            synchronized (lock) {
                List<String> allUsername = userService.getAllUsername();
                String username = UsernameGenerateUtils.getUsername(4, 6, allUsername);
                userEntity.setUsername(username);
                userEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
                String password = userEntity.getPassword();
                if (StringUtils.isNotBlank(password)) {
                    userEntity.setPassword(DigestUtils.md5Hex(password));
                }
                userEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                userEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
                if (userService.insertUser(userEntity) != 0) {
                    return CommonResult.success();
                }
            }

        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteUser (@PathVariable Integer id, @RequestBody UserEntity userEntity){
        if (userEntity != null) {
            userEntity.setId(id);
            userEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            userEntity.setDeleteFlag(DeleteFlagType.CANCEL.getId());
            if (userService.updateUser(userEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }
}
