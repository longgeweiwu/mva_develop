package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.IntelligentAsrEntity;

import java.util.List;

public interface IntelligentAsrMapper extends BaseMapper<IntelligentAsrEntity> {


    void generateBaseTable();

    List<String> getRecordsToAsr();
}
