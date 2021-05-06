package com.libseat.server.web.service.impl;


import com.libseat.api.entity.RankEntity;
import com.libseat.api.model.RankNode;
import com.libseat.server.web.dto.RankInfo;
import com.libseat.server.web.mapper.RankMapper;
import com.libseat.server.web.service.RankService;
import com.libseat.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.LinkedList;
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
        return rankMapper.updateByPrimaryKeySelective(rankDBById);
    }

    @Override
    public RankInfo getRank(String flag, Integer customerId) {
        Jedis jedis = null;
        int rank = -1;
        RankInfo rankInfo = null;
        try {
            jedis = redisUtil.getJedis();
            rank = jedis.zrank(flag,customerId.toString()).intValue() + 1;
            rankInfo = getCustomerRankById(customerId);
            if (rankInfo != null) {
                rankInfo.setRank(rank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return rankInfo;
    }

    @Override
    public Integer insertRank(RankEntity rankEntity) {
        return rankMapper.insert(rankEntity);
    }

    @Override
    public RankEntity getRankByCustomerId(Integer id) {
        RankEntity rankEntity = new RankEntity();
        rankEntity.setCustomerId(id);
        return rankMapper.selectOne(rankEntity);
    }

    @Override
    public List<RankInfo> getRanks(String flag) {
        List<RankNode> ranksTopN = getRanksTopN(flag, 50);
        List<RankInfo> rankInfos = new LinkedList<>();
        if (ranksTopN != null && !ranksTopN.isEmpty()) {
            ranksTopN.forEach(rankNode -> {
                rankInfos.add(getRank(flag,rankNode.getId()));
            });
        }
        return rankInfos;
    }

    @Override
    public RankInfo getCustomerRankById(Integer id) {
        return rankMapper.getCustomerRankById(id);
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
