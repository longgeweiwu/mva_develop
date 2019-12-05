package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.AgentScore;

import java.util.List;

public interface AgentScoreMapper extends BaseMapper<AgentScore> {

    List<AgentScore> getList();
}
