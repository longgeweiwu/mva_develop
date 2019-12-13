package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.entity.QuarkCallbackEntity;

import java.util.List;

public interface QuarkCallbackMapper extends BaseMapper<QuarkCallbackEntity> {

    /**
     *
     * @param top 查询多少条
     * @return
     */
    public List<QuarkCallbackEntity> queryIflyPendingTopMapper(int top);


    void generateIflyBaseTable();
}
