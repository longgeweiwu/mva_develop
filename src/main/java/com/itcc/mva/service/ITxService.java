package com.itcc.mva.service;

import com.itcc.mva.entity.TxAsrEntity;

import java.util.List;

/**
 * @author whoami
 */
public interface ITxService {

    /**
     * copy to txbasetable
     */
    void generateTxBaseTable();


    /**
     * create job visit ali web
     * @param top 查询多少条
     * @return
     */
    List<TxAsrEntity> queryTxFileTop(int top);

    /**
     * upload file to server
     * @return
     */
    void uploadTxFile(TxAsrEntity txAsrEntity);


    /**
     * create job visit TX web
     * @param top 查询多少条
     * @return
     */
    List<TxAsrEntity> queryTxPendingTop(int top);


    /**
     * create job visit TX web
     * 单条解析文本
     */
    void addTxTask(TxAsrEntity txAsrEntity);

    /**
     * create job visit TX web
     * @param top 查询多少条
     * @return
     */
    List<TxAsrEntity> queryTxResultTop(int top);


    /**
     * create job visit Tx web
     * 单条解析文本
     */
    void queryAndSetTxBase(TxAsrEntity txAsrEntity);

}
