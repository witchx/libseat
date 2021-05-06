package com.libseat.server.web.controller;


import com.libseat.api.entity.RoomEntity;
import com.libseat.api.entity.SeatEntity;
import com.libseat.api.model.Seat;
import com.libseat.server.web.service.RoomService;
import com.libseat.server.web.service.SeatService;
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

    @GetMapping(value = "/all")
    @ResponseBody
    public CommonResult<Seat[]> getSeatList (@RequestParam Integer roomId, @RequestParam Long startTime, @RequestParam Long endTime){
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(roomId);
        roomEntity = roomService.getRoomById(roomEntity);
        if (roomEntity == null) {
            return CommonResult.failed();
        }
        Integer line = roomEntity.getLine();
        Integer row = roomEntity.getRow();
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setRoomId(roomId);
        List<SeatEntity> seatList = seatService.getSeatList(roomId, new Timestamp(startTime), new Timestamp(endTime));
        if (seatList == null || seatList.isEmpty()) {
            return CommonResult.failed("系统出错");
        }
        return CommonResult.success(seatService.unparkSeat(seatList, line, row));
    }
}
