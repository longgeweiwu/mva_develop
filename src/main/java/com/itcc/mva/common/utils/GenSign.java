package com.itcc.mva.common.utils;

import com.inspur.est.signsec.utils.Md5DataSign;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Whoami
 */
public class GenSign {

    public static Map<String,Object> getValidSign(){
        Map<String, Object> result = new HashMap<String, Object>();
        //appId
        String appId = "incoming_phone";
        //appSecret
        String appSecret="0978cb19aea045518138ea5ef2c14075";
        //加密盐值
        String salt = "719a4fa475594f129b7ebfbdabd54369";
        //当前时间戳
        long t = System.currentTimeMillis();

        //组装带加密的数据
        String signStr ="appId"+appId+"t"+t+"appSecret"+appSecret;
        //进行加密
        Md5DataSign md5Sign = new Md5DataSign();
        String sign = md5Sign.sign(salt, signStr);
        result.put("t",String.valueOf(t));
        result.put("sign",sign);
        return result;
    }



    public static Map<String,Object> getValidSign(String data){
        Map<String, Object> result = new HashMap<String, Object>();
        //appId
        String appId = "incoming_phone";
        //appSecret
        String appSecret="0978cb19aea045518138ea5ef2c14075";
        //加密盐值
        String salt = "719a4fa475594f129b7ebfbdabd54369";
        //当前时间戳
        long t = System.currentTimeMillis();

        //组装带加密的数据
        String signStr ="appId"+appId+"t"+t+"appSecret"+appSecret
                +"data"+data;
        //进行加密
        Md5DataSign md5Sign = new Md5DataSign();
        String sign = md5Sign.sign(salt, signStr);
        result.put("t",String.valueOf(t));
        result.put("sign",sign);
        return result;
    }
}
