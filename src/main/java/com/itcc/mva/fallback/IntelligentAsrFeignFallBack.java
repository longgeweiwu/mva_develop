package com.itcc.mva.fallback;

import com.alibaba.fastjson.JSONObject;
import com.itcc.mva.feign.IntelligentAsrFeign;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public class IntelligentAsrFeignFallBack implements IntelligentAsrFeign {
    @Override
    public JSONObject asrResult(JSONObject records) {
        return null;
    }

    @Override
    public JSONObject asrStatus(JSONObject taskDetail) {
        return null;
    }
}
