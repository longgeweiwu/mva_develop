package com.itcc.mva.rest;

import com.alibaba.fastjson.JSON;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.service.IQuarkCallbackService;
import com.itcc.mva.vo.QuarkCallbackVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whoami
 */
@RestController
@Slf4j
@RequestMapping(value = "/quark")
public class QuarkCallbackController {
    private int size = 0;
    private Map<String, String> results = new HashMap<>();
    /**
     * 接收离线转写结果通知
     *
     * @param request
     * @param id 唯一Id
     * @return
     */
    @RequestMapping(value = "/callback/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String orderCallback(HttpServletRequest request, @PathVariable("id") String id) {
        //这里要读取request的body！！！
        StringBuilder buffer = new StringBuilder();
        //读取Body
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            log.error("回调 异常: {}", e.getMessage());
        }
        String body = buffer.toString();

        QuarkCallbackVo resp = JSON.parseObject(body, QuarkCallbackVo.class);  //解析数据也变成了body，而不是原来的result！！！
        StringBuffer resultBuff = new StringBuffer("");

        if (resp.getLattice() == null) {
            log.info("转写错误：{}", resp.getAid());
            return "success";
        }
        for (QuarkCallbackVo.Lattice l : resp.getLattice()) {
            Tools.parseReuslt(resultBuff, l.getJson_1best());
        }
        if (!results.containsKey(resp.getAid())) {
            results.put(resp.getAid(), Long.toString(System.currentTimeMillis()));
            int i = size++;
        }
        log.info("解析转写结果,处理总数:{} aid:{},result:{}", size, resp.getAid(), resultBuff.toString());
        return "success";
    }



}
