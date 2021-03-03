package com.libseat.admin.service.impl;


import com.libseat.admin.mapper.RankMapper;
import com.libseat.admin.service.RankService;
import com.libseat.api.constant.Constant;
import com.libseat.api.entity.RankEntity;
import com.libseat.api.model.RankNode;
import com.libseat.utils.redis.RedisUtil;
import com.libseat.utils.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * zset 是从0开始，升序排序的， 所有要在此基础上  time取反， 排名+1，
 */
@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RankMapper rankMapper;

    @Override
    public void initRank() {
        List<RankEntity> rankListDB = getRankListDB();
        int flag = DateUtils.getWeeksBetweenToday(Constant.OPEN_DAY);
        int lastFlag = flag - 1;
        //先清理
        clearRank(Constant.RANK_MONTH+lastFlag);
        clearRank(Constant.RANK_WEEK+lastFlag);
        clearRank(Constant.RANK_YEAR+lastFlag);
        //装入Redis
        rankListDB.forEach(rankEntity -> {
            updateRank(Constant.RANK_WEEK+flag,rankEntity.getCustomerId(),rankEntity.getHoursByWeek());
            updateRank(Constant.RANK_MONTH+flag,rankEntity.getCustomerId(),rankEntity.getHoursByMonth());
            updateRank(Constant.RANK_YEAR+flag,rankEntity.getCustomerId(),rankEntity.getHoursByYear());
        });
    }

    @Override
    public List<RankEntity> getRankListDB() {
        return rankMapper.selectAll();
    }

    @Override
    public RankEntity getRankDBById(Integer id) {
        RankEntity rankEntity = new RankEntity();
        rankEntity.setId(id);
        return rankMapper.selectByPrimaryKey(rankEntity);
    }

    @Override
    public Integer updateRankDB(Integer id, Double addTime, Integer addDay) {
        RankEntity rankDBById = getRankDBById(id);
        rankDBById.setHoursByWeek(rankDBById.getHoursByWeek()+addTime);
        rankDBById.setHoursByMonth(rankDBById.getHoursByMonth()+addTime);
        rankDBById.setHoursByYear(rankDBById.getHoursByYear()+addTime);
        rankDBById.setDaysByWeek(rankDBById.getDaysByWeek()+addDay);
        rankDBById.setDaysByMonth(rankDBById.getDaysByMonth()+addDay);
        rankDBById.setDaysByYear(rankDBById.getDaysByYear()+addDay);
        return rankMapper.updateByPrimaryKeySelective(rankDBById);
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
    public void updateRank(String flag, Integer customerId, Double time) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            //放入sorted set
            jedis.zadd(flag, -time, customerId.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    @Override
    public RankNode getRank(String flag, Integer customerId) {
        Jedis jedis = null;
        int rank = -1;
        double time = 0D;
        try {
            jedis = redisUtil.getJedis();
            rank = jedis.zrank(flag,customerId.toString()).intValue() + 1;
            time = Math.abs(jedis.zscore(flag,customerId.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return new RankNode(customerId,time,rank);
    }

    public List<RankNode> getRanksTopN(String flag, int n) {
        Jedis jedis = null;
        List<RankNode> rankNodes = new ArrayList<>();
        AtomicInteger rank = new AtomicInteger(1);
        try {
            jedis = redisUtil.getJedis();
            Set<Tuple> tuples = jedis.zrangeWithScores(flag, 0, n - 1);
            tuples.forEach(tuple -> {
                rankNodes.add(new RankNode(Integer.valueOf(tuple.getElement()),Math.abs(tuple.getScore()),rank.getAndIncrement()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return rankNodes;
    }
}
