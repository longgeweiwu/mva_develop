package com.itcc.mva.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Tools {
    /**
     * 查找本地包含字符串的文件名
     * @param path
     * @param fname
     * @return
     */
    public static String findTrueFname(String path,String fname) {
        String trueFname="";
        File file = new File(path);
        File[] fileList = file.listFiles();
        for(int i=0;i<fileList.length;i++) {
            if(fileList[i].toString().contains(fname)) {
                trueFname=fileList[i].toString();
                break;
            }
        }
        return trueFname.substring(trueFname.lastIndexOf("\\")+1, trueFname.length());
    }

    /**
     * 以分隔符 取 最大数组值
     * @param value
     * @param split
     * @return
     */
    public static String getSplitMaxValue(String value,String split) {
        return value.split(split)[value.split(split).length-1];
    }

    /**
     * 暴力解析:Alibaba fastjson
     * @param test
     * @return
     */
    public final static boolean isJSONValid(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    /**
     * @return 32位随机数
     */
    public static String getUniqueID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /***
     * 解析回调报文
     * @param result
     * @return
     */
    public static void parseReuslt(StringBuffer resultBuff, String result) {
        if (StringUtils.isEmpty(result)) {
            return;
        }
        JSONObject json = JSON.parseObject(result);
        JSONObject st = json.getJSONObject("st");    //句子
        String rl = st.getString("rl"); //角色
        JSONArray rtArray = st.getJSONArray("rt");
        resultBuff.append(rl).append(":");
        if (rtArray != null && rtArray.size() != 0) {
            for (Object rt : rtArray) {
                JSONArray wsArray = ((JSONObject) rt).getJSONArray("ws");   //表示开始输出词语识别结果
                if (wsArray != null && wsArray.size() != 0) {
                    for (Object ws : wsArray) {
                        JSONArray cwArray = ((JSONObject) ws).getJSONArray("cw");   //词语候选识别结果
                        for (Object cw : cwArray) {
                            String w = ((JSONObject) cw).getString("w");    //识别结果
                            resultBuff.append(w);
                        }
                    }
                }
            }
        }
        resultBuff.append("\r\n");
    }

    /**
     * 添加离线转写任务
     * @param wavcid    任务唯一Id
     * @param audiourl  音频文件下载地址
     * @param notifyUrl 转写结果通知url地址
     */
    public static void addTask(String wavcid,String serverurl,String audiourl,String notifyUrl){
        long start=System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("wavcid", wavcid);
        map.put("file_db_type", "http");
        map.put("http_file", audiourl);
        map.put("proc_complite_notify_http_url",notifyUrl);
        map.put("eparam", "sample_type=16k;vspp_on=1");
        map.put("quark_procer_action", "process");
        map.put("action", "quark_procer");
        long netstart=System.currentTimeMillis();
        String resp = HttpUtil.get(serverurl, map);
        long end=System.currentTimeMillis()-start;
        long netEnd=System.currentTimeMillis()-netstart;
        System.out.println("添加离线转写任务taskId:"+wavcid+",总耗时："+end +"网络耗时:"+netEnd+" 响应结果:"+resp);
    }

    /**
     * 添加异步转码任务
     * @param taskId    任务Id
     * @param serverUrl 转码服务器url
     * @param notifyUrl 转码消息通知地址
     * @param uploadUrl 转码后的文件上传服务器地址
     * @param downloadUrl   待转码文件下载地址
     */
    public static void addRmaTask(String taskId,String serverUrl,String notifyUrl,String uploadUrl,String downloadUrl){
        Map<String, Object> map = new HashMap<>();
        map.put("wavcid", taskId);
        map.put("file_db_type_wav", "http");
        map.put("file_db_type_ori", "http");
        map.put("proc_complite_notify_http_url", notifyUrl);
        map.put("http_file_ori", downloadUrl);
        map.put("http_file_wav", uploadUrl);
        map.put("rma_procer_action", "convert");
        map.put("action", "rma_procer");
        String resp = HttpUtil.get(serverUrl, map);
        System.out.println("添加格式转换完成:taskId："+taskId +"响应结果:"+resp);

    }

    /**
     * 判断身份证是否合法
     * @param id    身份证号码
     * @return  true:合法,false:不合法
     */
    public static boolean isLegal(String id) {
        int sum = 0;
        char[] chekBit = {'1','0','X','9','8','7','6','5','4','3','2'};
        int[] power = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
        char[] strinigAll = id.toCharArray();
        for (int i = 0; i < 17; i++) {
            sum += power[i]*(strinigAll[i] - '0');
        }
        if(strinigAll[17] == chekBit[sum%11]){
            return true;
        }
        return false;
    }
}
