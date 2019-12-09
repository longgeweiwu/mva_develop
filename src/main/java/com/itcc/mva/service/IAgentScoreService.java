package com.itcc.mva.service;

import com.itcc.mva.entity.AgentScore;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IAgentScoreService {

    List<AgentScore> getAgentList();

    @Select("<script>" + "SELECT * FROM T_QC_AGENTSCORE" +
            "</script>")
    List<AgentScore> getSqlAccess();
}
