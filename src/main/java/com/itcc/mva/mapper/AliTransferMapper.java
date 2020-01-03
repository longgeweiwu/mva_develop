package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.AliAsrEntity;

import java.util.List;


public interface AliTransferMapper extends BaseMapper<AliAsrEntity> {
    void generateAliBaseTable();

    /**
     *
     * @param top 查询多少条
     * @return
     */
    List<AliAsrEntity> queryAliPendingTopMapper(int top);

    /**
     *
     * @param top 查询多少条
     * @return
     */
    List<AliAsrEntity> queryAliResultTopMapper(int top);
}
