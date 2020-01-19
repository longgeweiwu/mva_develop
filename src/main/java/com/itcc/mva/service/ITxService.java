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

}
