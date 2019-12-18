package com.itcc.mva.feign;

import com.alibaba.fastjson.JSONObject;
import com.itcc.mva.fallback.IntelligentAsrFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "downloadRecord",url = "${asrParams.recordsUrl}",fallback = IntelligentAsrFeignFallBack.class)
public interface IntelligentAsrFeign {
    @RequestMapping(value = "/speechAnalysis",method = RequestMethod.POST,consumes = "application/json")
    JSONObject asrResult(JSONObject recordsDetail);
    @RequestMapping(value = "/getStatus",method = RequestMethod.POST,consumes = "application/json")
    JSONObject asrStatus(JSONObject taskDetail);
}
