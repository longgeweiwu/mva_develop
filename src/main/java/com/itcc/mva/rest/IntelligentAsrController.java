package com.itcc.mva.rest;

import com.itcc.mva.service.IIntelligentTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LCL
 */
@RestController
@RequestMapping("/mva/intelligentAsr")
@EnableScheduling
@Slf4j
public class IntelligentAsrController {

    @Autowired
    private IIntelligentTransferService intelligentTransferService;

    /**
     * 生产录音解析基表
     * @Author LCL
     */
    @GetMapping("/generateBaseTable")
    public String generateBaseTable() {
        intelligentTransferService.generateBaseTable();
        log.info("intelligentTransferAnalysis调用了***************************");
        return "ok";
    }

    /**
     * 录音解析
     * @Author LCL
     */
    @GetMapping("/asr")
    public String asr() {
        intelligentTransferService.asr();
        log.info("IntelligentAsrController.asr调用了***************************");
        return "ok";
    }
}
