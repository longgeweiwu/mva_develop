package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.QuarkCallbackEntity;

import java.util.List;
/**
 * @author whoami
 */
public interface QuarkCallbackMapper extends BaseMapper<QuarkCallbackEntity> {

    /**
     *
     * @param top 查询多少条
     * @return
     */
    List<QuarkCallbackEntity> queryIflyPendingTopMapper(int top);

    /**
     *
     * @param top 查询待转录音多少条
     * @return
     */
    List<QuarkCallbackEntity> pushToIflyAudioTopMapper(int top);
    /**
     *
     * @param top 查询待转录音多少条
     * @return
     */
    List<QuarkCallbackEntity> queryIstIflyPendingTopMapper(int top);

    void generateIflyBaseTable();
}
