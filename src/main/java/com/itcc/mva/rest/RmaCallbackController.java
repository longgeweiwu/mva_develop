package com.itcc.mva.rest;

import com.itcc.mva.common.utils.Constant;
import lombok.extern.slf4j.Slf4j;
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
 * 音频文件转码回调服务
 */
@RestController
@Slf4j
@RequestMapping(value = "/rma")
public class RmaCallbackController {
    private int size=0;
    private Map<String,String> results=new HashMap<>();

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
        System.out.println("消息通知:"+message);
        log.debug("消息通知:{}",message);
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
        String filename= Constant.RMAPATH +File.separator+"16k_"+id+".wav";
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
        return "SUCCESS";
    }
}
