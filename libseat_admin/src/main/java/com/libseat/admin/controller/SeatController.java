package com.libseat.admin.controller;

import com.libseat.admin.service.RoomService;
import com.libseat.admin.service.SeatService;
import com.libseat.api.entity.RoomEntity;
import com.libseat.api.entity.SeatEntity;
import com.libseat.utils.code.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@CrossOrigin
@RequestMapping("/api/seat")
@Controller
public class SeatController {


    @Autowired
    private SeatService seatService;
    @Autowired
    private RoomService roomService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Object> getSeatList (@RequestParam Integer roomId, @RequestParam Long startTime, @RequestParam Long endTime){
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(roomId);
        roomEntity = roomService.getRoomById(roomEntity);
        if (roomEntity == null) {
            return CommonResult.failed("没有自习室信息");
        }
        Integer line = roomEntity.getLine();
        Integer row = roomEntity.getRow();
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setRoomId(roomId);
        List<SeatEntity> seatList = seatService.getSeatList(roomId, new Timestamp(startTime), new Timestamp(endTime));
        if (seatList == null || seatList.isEmpty()) {
            return CommonResult.failed("系统错误");
        }
        return CommonResult.success(seatService.unparkSeat(seatList, line, row));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateSeat (@RequestBody RoomEntity roomEntity){
        if (roomEntity != null) {
            if (roomService.getRoomById(roomEntity) == null ) {
                return CommonResult.failed("没有当前自习室！");
            } else {
                try {
                    seatService.updateSeatList(seatService.assembleSeats(roomEntity.getSeat(), roomEntity.getId()));
                    return CommonResult.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    return CommonResult.failed(e.getMessage());
                }
            }
        }
        return CommonResult.failed();
    }
}
