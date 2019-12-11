package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.vo.RecordNameAndPathVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IntelligentAsrMapper extends BaseMapper<IntelligentAsrEntity> {


    void generateBaseTable();

    List<RecordNameAndPathVo> getRecordsToAsr();

    void asrSuccess(@Param("params") Map<String,String> params);

    void asrFail(String recordFileName);
}
