package com.itcc.mva.feign;

import com.alibaba.fastjson.JSONObject;
import com.itcc.mva.fallback.IntelligentAsrFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "downloadRecord",url = "${asrParams.recordsUrl}",fallback = IntelligentAsrFeignFallBack.class)
public interface IntelligentAsrFeign {
    @PostMapping(value = "/speechAnalysis")
    JSONObject asrResult(JSONObject recordsDetail);
    @PostMapping(value = "/getStatus")
    JSONObject asrStatus(JSONObject taskDetail);
}
