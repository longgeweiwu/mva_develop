package com.itcc.mva.service.impl;

import com.itcc.mva.entity.AgentScore;
import com.itcc.mva.mapper.AgentScoreMapper;
import com.itcc.mva.service.IAgentScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentScoreServiceImpl implements IAgentScoreService {
    @Autowired
    private AgentScoreMapper agentScoreMapper;

    @Override
    public List<AgentScore> getAgentList() {
        return agentScoreMapper.getList();
    }
}