package com.itcc.mva.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author whoami
 * 如果需要回调在细化代码
 */
@RestController
@RequestMapping("/ali")
public class AliCallbackController {

    @RequestMapping(value = "/callback",method = {RequestMethod.POST, RequestMethod.GET})
    public String notifyCallback(HttpServletRequest request, @RequestBody String message){
        System.out.println("ContentType:"+request.getContentType());
        try {
            message= URLDecoder.decode(message,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("转码结果通知:"+message);
        JSONObject jsonObject= JSON.parseObject(message.replaceAll("=",""));
        return "200";

    }
}
