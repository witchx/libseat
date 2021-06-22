package com.libseat.admin.service.impl;


import com.alibaba.fastjson.JSON;
import com.libseat.admin.mapper.RankMapper;
import com.libseat.admin.service.OrderSeatService;
import com.libseat.admin.service.OrderService;
import com.libseat.admin.service.RankService;
import com.libseat.api.constant.Constant;
import com.libseat.api.entity.OrderEntity;
import com.libseat.api.entity.OrderSeatEntity;
import com.libseat.api.entity.RankEntity;
import com.libseat.api.entity.RankInfo;
import com.libseat.utils.redis.RedisUtil;
import com.libseat.utils.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.sql.Timestamp;
import java.util.List;

/**
 * zset 是从0开始，升序排序的， 所有要在此基础上  time取反， 排名+1，
 */
@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RankMapper rankMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSeatService orderSeatService;

    @Override
    public void initRank() {
        List<RankInfo> rankInfos = getRankInfoListDB();
        int flag = DateUtils.getWeeksBetweenToday(Constant.OPEN_DAY);
        int lastFlag = flag - 1;
        //先清理
        clearRank(Constant.RANK_MONTH+lastFlag);
        clearRank(Constant.RANK_WEEK+lastFlag);
        clearRank(Constant.RANK_YEAR+lastFlag);
        clearRank(Constant.RANK_MONTH+flag);
        clearRank(Constant.RANK_WEEK+flag);
        clearRank(Constant.RANK_YEAR+flag);
        //装入Redis
        rankInfos.forEach(rankInfo -> {
            String rankStr = JSON.toJSONString(rankInfo);
            updateRank(Constant.RANK_WEEK+flag ,rankInfo.getHoursByWeek() ,rankStr);
            updateRank(Constant.RANK_MONTH+flag,rankInfo.getHoursByMonth(),rankStr);
            updateRank(Constant.RANK_YEAR+flag ,rankInfo.getHoursByYear() ,rankStr);
        });
    }

    /**
     * 清除rank
     * @param flag
     * @return
     */
    @Override
    public Boolean clearRank(String flag) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            //清除sorted set
            jedis.zremrangeByRank(flag, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;
    }


    @Override
    public void updateRank(String flag,Double time,String rank) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            //放入sorted set
            jedis.zadd(flag, -time, rank);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public List<RankEntity> getRankListDB() {
        return rankMapper.selectAll();
    }

    @Override
    public List<RankInfo> getRankInfoListDB() {
        return rankMapper.getRankInfoList();
    }

    @Override
    public RankEntity getRankDBById(Integer id) {
        RankEntity rankEntity = new RankEntity();
        rankEntity.setId(id);
        return rankMapper.selectByPrimaryKey(rankEntity);
    }

    @Override
    public Integer updateRankDB(Integer customerId, Double addTime, Integer addDay) {
        RankEntity rankDBById = getRankByCustomerId(customerId);
        rankDBById.setHoursByWeek(rankDBById.getHoursByWeek()+addTime);
        rankDBById.setHoursByMonth(rankDBById.getHoursByMonth()+addTime);
        rankDBById.setHoursByYear(rankDBById.getHoursByYear()+addTime);
        rankDBById.setDaysByWeek(rankDBById.getDaysByWeek()+addDay);
        rankDBById.setDaysByMonth(rankDBById.getDaysByMonth()+addDay);
        rankDBById.setDaysByYear(rankDBById.getDaysByYear()+addDay);
        rankDBById.setModifyTime(new Timestamp(System.currentTimeMillis()));
        return rankMapper.updateByPrimaryKeySelective(rankDBById);
    }

    @Override
    public void clearWeekRank() {
        List<RankEntity> rankListDB = getRankListDB();
        rankListDB.forEach(rankEntity -> {
            rankEntity.setDaysByWeek(0);
            rankEntity.setHoursByWeek(0.0);
            rankMapper.updateByPrimaryKey(rankEntity);
        });
    }

    @Override
    public Integer insertRank(RankEntity rankEntity) {
        return rankMapper.insertUseGeneratedKeys(rankEntity);
    }

    @Override
    public RankEntity getRankByCustomerId(Integer id) {
        RankEntity rankEntity = new RankEntity();
        rankEntity.setCustomerId(id);
        return rankMapper.selectOne(rankEntity);
    }

    @Override
    public void addStudyTime(Integer orderId) {
        OrderSeatEntity orderSeatEntity = orderSeatService.getOrderByOrderId(orderId);
        long time = orderSeatEntity.getEndTime().getTime() - orderSeatEntity.getStartTime().getTime();
        double addTime = (double) time/ DateUtils.HOUR;
        OrderEntity orderEntity = orderService.getOrderById(orderId);
        updateRankDB(orderEntity.getCustomerId(),addTime,1);
    }
}
