package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.RoomService;
import com.libseat.admin.service.SeatService;
import com.libseat.api.entity.RoomEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@CrossOrigin
@RequestMapping("/api/room")
@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private SeatService seatService;

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<RoomEntity>> getRoomList (@RequestParam(required = false) String name,
                                                             @RequestParam(required = false) Integer stadiumId,
                                                             @RequestParam(required = false) String stadiumName,
                                                             @RequestParam(required = false, defaultValue = "1") Integer page,
                                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<RoomEntity> roomList = roomService.getRoomList(name, stadiumId, stadiumName, page, pageSize);
        if (roomList == null || roomList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(roomList);
        }
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<RoomEntity>> getRoomListByStadiumId (@RequestParam Integer stadiumId,
                                                                        @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<RoomEntity> roomList = roomService.getRoomListByStadiumId(stadiumId, page, pageSize);
        if (roomList == null || roomList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(roomList);
        }
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateRoom (@PathVariable Integer id, @RequestBody RoomEntity roomEntity){
        if (roomEntity != null) {
            if (StringUtils.isNotBlank(roomEntity.getName())) {
                roomEntity.setId(id);
                roomEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
                if (roomService.updateRoom(roomEntity) != 0) {
                    return CommonResult.success();
                }
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createRoom ( @RequestBody RoomEntity roomEntity) {
        if (roomEntity != null) {
            try {
                roomEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                roomEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
                roomService.insertRoomAndSeat(roomEntity);
                return CommonResult.success();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteRoom (@PathVariable Integer id){
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(id);
        roomService.deleteRoom(roomEntity);
        return CommonResult.failed();
    }
}
