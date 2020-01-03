package com.itcc.mva.service;

import com.itcc.mva.entity.AliAsrEntity;

import java.util.List;

/**
 * @author whoami
 */
public interface IAliTransferService {

    /**
     * copy to alibasetable
     */
    void generateAliBaseTable();


    /**
     * create job visit ali web
     * @param top 查询多少条
     * @return
     */
    List<AliAsrEntity> queryAliPendingTop(int top);

    /**
     * create job visit ali web
     * 单条解析文本
     */
    void addAliTask(AliAsrEntity aliAsrEntity);


    /**
     * create job visit ali web
     * @param top 查询多少条
     * @return
     */
    List<AliAsrEntity> queryAliResultTop(int top);


    /**
     * create job visit ali web
     * 单条解析文本
     */
    void queryAndSetAliBase(AliAsrEntity aliAsrEntity);


}
