package com.itcc.mva.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mva/test")
public class Test {
    @GetMapping("/queryByPage")
    public String queryAgentOutByPage() {
        return "success";
    }

}
