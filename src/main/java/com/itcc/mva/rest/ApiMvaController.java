package com.itcc.mva.rest;

import com.itcc.mva.service.IPushToMvaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/verify")
public class ApiMvaController {
    @Autowired
    private IPushToMvaService iPushToMvaService;

    @GetMapping(value = "/illega", produces = { "application/xml;charset=UTF-8" })
    public String queryMission(@RequestParam(value="session.sce.id") String id) {
     return iPushToMvaService.illegalId(id);
    }
}
