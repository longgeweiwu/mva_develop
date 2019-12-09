package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.AgentScore;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AgentScoreMapper extends BaseMapper<AgentScore> {

    List<AgentScore> getList();


    @Select("<script>" + "SELECT * FROM T_QC_AGENTSCORE" +
            "</script>")
    List<AgentScore> getSqlList();
}
