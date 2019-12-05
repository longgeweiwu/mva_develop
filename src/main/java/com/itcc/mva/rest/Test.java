package com.itcc.mva.rest;

import com.itcc.mva.entity.AgentScore;
import com.itcc.mva.service.IAgentScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mva/test")
public class Test {
    @Autowired
    private IAgentScoreService iAgentScoreService;

    @GetMapping("/queryByPage")
    public List<AgentScore> queryAgentOutByPage() {
        return iAgentScoreService.getAgentList();
    }

}
