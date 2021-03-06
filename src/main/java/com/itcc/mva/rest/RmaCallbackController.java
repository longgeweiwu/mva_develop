package com.itcc.mva.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.service.IQuarkCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whoami
 * 音频文件转码回调服务
 */
@RestController
@Slf4j
@RequestMapping(value = "/rma")
public class RmaCallbackController {

    @Autowired
    private IQuarkCallbackService iQuarkCallbackService;

    @RequestMapping(value = "/upload",method = {RequestMethod.POST, RequestMethod.GET})
    public String orderCallback(HttpServletRequest request){
        return null;
    }

    /**
     * 用于接收转码结果通知
     * @param message 转码消息内容
     * @return
     */
    @RequestMapping(value = "/callback",method = {RequestMethod.POST, RequestMethod.GET})
    public String notifyCallback(HttpServletRequest request, @RequestBody String message){
        System.out.println("ContentType:"+request.getContentType());
        try {
            message=URLDecoder.decode(message,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("转码结果通知:"+message);
        JSONObject jsonObject= JSON.parseObject(message.replaceAll("=",""));
        iQuarkCallbackService.updateRmaVoiceFlag(jsonObject.getString("aid"),Constant.RMA_IFLY_SUCCESS);
        log.debug("转码结果通知:{}",message);
        return "200";

    }

    /**
     * 用于接收文件上传
     */
    @ResponseBody
    @RequestMapping(value = "/upload/{id}",method = RequestMethod.POST)
    public String photoUpload(@PathVariable(name = "id") String id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IllegalStateException, IOException{
        // 检测是否为多媒体上传
        System.out.println("ContentType:"+request.getContentType());
        System.out.println("ContentLength:"+request.getContentLength());
        ServletInputStream in = request.getInputStream();
        byte[] buff=new byte[1024];
        int count=0;
        /**
         * 程序服务器的录音地址！！！
         */
        String RMAPATH=iQuarkCallbackService.getVoidPath(id+".wav");
        String filename= RMAPATH +"16k_"+id+".wav";
        File file=new File(filename);
        FileOutputStream out=new FileOutputStream(file);
        while ((count=in.read(buff))!=-1){
            if (count<1024){
                out.write(buff,0,count);
            }else{
                out.write(buff);
            }
        }
        out.flush();
        out.close();
        System.out.println("文件存储地址："+filename);
        iQuarkCallbackService.updateRmaVoiceName(id+".wav",filename);
        return "SUCCESS";
    }
}
