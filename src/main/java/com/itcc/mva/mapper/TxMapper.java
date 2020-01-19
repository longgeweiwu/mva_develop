package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.TxAsrEntity;

import java.util.List;

/**
 * @author whoami
 */
public interface TxMapper extends BaseMapper<TxAsrEntity> {
    void generateTxBaseTable();

    /**
     *
     * @param top 查询要上传的文件多少条
     * @return
     */
    List<TxAsrEntity> queryTxFileTop(int top);

    /**
     * 查询离线转写
     * @param top
     * @return
     */
    List<TxAsrEntity> queryTxPendingTop(int top);

    /**
     * 查询离线转写
     * @param top
     * @return
     */
    List<TxAsrEntity> queryTxResultTop(int top);
}
