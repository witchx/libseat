package com.libseat.server.web.controller;


import com.libseat.api.entity.RoomEntity;
import com.libseat.server.web.dto.RoomInfo;
import com.libseat.server.web.service.RoomService;
import com.libseat.utils.code.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@CrossOrigin
@RequestMapping("/api/room")
@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping(value = "/all")
    @ResponseBody
    public CommonResult<List<RoomInfo>> getRoom (@RequestParam Integer stadiumId,
                                                 @RequestParam Long startTime,
                                                 @RequestParam Long endTime){

        List<RoomInfo> roomList = roomService.getRoomList(stadiumId,
                new Timestamp(startTime),
                new Timestamp(endTime));
        if (roomList == null) {
            return CommonResult.failed();
        } else {
            return CommonResult.success(roomList);
        }
    }

    @GetMapping(value = "/name")
    @ResponseBody
    public CommonResult<String> getRoom (RoomEntity roomEntity){

        RoomEntity roomById = roomService.getRoomById(roomEntity);
        if (roomById == null) {
            return CommonResult.failed();
        } else {
            return CommonResult.success(roomById.getName());
        }
    }
}
