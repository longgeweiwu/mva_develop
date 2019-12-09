package com.itcc.mva.rest;

import com.itcc.mva.entity.AgentScore;
import com.itcc.mva.service.IAgentScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mva/api")
public class demoApi {
    @Autowired
    private IAgentScoreService iAgentScoreService;

    @GetMapping("/demo")
    public List<AgentScore> queryAgentOutByPage() {
        return iAgentScoreService.getAgentList();
    }


    @GetMapping("/demo2")
    public List<AgentScore> querysql() {
        return iAgentScoreService.getSqlAccess();
    }


}
