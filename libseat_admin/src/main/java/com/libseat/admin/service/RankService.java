package com.libseat.admin.service;


import com.libseat.api.entity.RankEntity;
import com.libseat.api.model.RankNode;

import java.util.List;

public interface RankService {

    void initRank();

    List<RankEntity> getRankListDB();

    RankEntity getRankDBById(Integer id);

    Integer updateRankDB(Integer id, Double addTime, Integer addDay);

    Boolean clearRank(String flag);

    void updateRank(String flag, Integer customerId, Double time);

    RankNode getRank(String flag, Integer userId);
}
