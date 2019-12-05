package com.itcc.mva.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcc.mva.entity.AgentScore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentScoreMapper extends BaseMapper<AgentScore> {

    List<AgentScore> getList();
}
