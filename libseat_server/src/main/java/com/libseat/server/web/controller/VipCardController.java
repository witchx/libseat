package com.libseat.server.web.controller;

import com.libseat.api.constant.VipCardType;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.server.web.service.VipCardService;
import com.libseat.utils.code.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.libseat.api.constant.VipCardType.*;

@CrossOrigin
@RequestMapping("/api/card")
@Controller
public class VipCardController {

    @Autowired
    private VipCardService vipCardService;

    private Logger logger = LoggerFactory.getLogger(VipCardController.class);

    @GetMapping("/all")
    @ResponseBody
    public CommonResult<Map<Integer, List<VipCardEntity>>> getAllVipCard(@RequestParam Integer id){
        Map<Integer,List<VipCardEntity>> map = new HashMap<>();
        List<VipCardEntity> values = new LinkedList<>();
        List<VipCardEntity> woulds = new LinkedList<>();
        List<VipCardEntity> times = new LinkedList<>();
        List<VipCardEntity> all = vipCardService.getAllVipCardByUserId(id);
        if (all == null || all.isEmpty()) {
            return CommonResult.failed();
        }
        all.forEach(vipCardEntity -> {
            VipCardType vipCardType = VipCardType.getById(vipCardEntity.getType());
            if (vipCardType ==null) {
                logger.error("vipCardId :"+ vipCardEntity.getId() +",vipCardType  error!!!!!!!");
            }
            switch (vipCardType) {
                case VALUE_CARD:
                    values.add(vipCardEntity);
                    break;
                case WOULD_CARD:
                    woulds.add(vipCardEntity);
                    break;
                case TIME_CARD:
                    times.add(vipCardEntity);
                    break;
                default:
                    break;
            }
            map.put(VALUE_CARD.getId(),values);
            map.put(WOULD_CARD.getId(),woulds);
            map.put(TIME_CARD.getId(),times);
        });
        return CommonResult.success(map);
    }

    @GetMapping("/my")
    @ResponseBody
    public CommonResult<Map<Integer, List<VipCardEntity>>> getMyVipCard(@RequestParam Integer id){
        Map<Integer,List<VipCardEntity>> map = new HashMap<>();
        List<VipCardEntity> values = new LinkedList<>();
        List<VipCardEntity> woulds = new LinkedList<>();
        List<VipCardEntity> times = new LinkedList<>();
        List<VipCardEntity> all = vipCardService.getAllVipCardByCustomerId(id);
        if (all == null || all.isEmpty()) {
            return CommonResult.failed();
        }
        all.forEach(vipCardEntity -> {
            VipCardType vipCardType = VipCardType.getById(vipCardEntity.getType());
            if (vipCardType ==null) {
                logger.error("vipCardId :"+ vipCardEntity.getId() +",vipCardType  error!!!!!!!");
            }
            switch (vipCardType) {
                case VALUE_CARD:
                    values.add(vipCardEntity);
                    break;
                case WOULD_CARD:
                    woulds.add(vipCardEntity);
                    break;
                case TIME_CARD:
                    times.add(vipCardEntity);
                    break;
                default:
                    break;
            }
            map.put(VALUE_CARD.getId(),values);
            map.put(WOULD_CARD.getId(),woulds);
            map.put(TIME_CARD.getId(),times);
        });
        return CommonResult.success(map);
    }
}
