package com.libseat.server.web.service;


import com.libseat.api.entity.RankEntity;
import com.libseat.server.web.dto.RankInfo;

import java.util.List;

public interface RankService {

    RankEntity getRankDBById(Integer id);

    Integer updateRankDB(Integer customerId, Double addTime, Integer addDay);

    RankInfo getRank(String flag, Integer customerId);

    Integer insertRank(RankEntity rankEntity);

    RankEntity getRankByCustomerId(Integer id);

    List<RankInfo> getRanks(String flag);

    RankInfo getCustomerRankById(Integer id);

}
