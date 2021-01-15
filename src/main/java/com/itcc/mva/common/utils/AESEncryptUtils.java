package com.itcc.mva.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class AESEncryptUtils {
    public static void main(String args[]) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("extIdcard","1101111010100100010");//身份证号码
        /**
         * 一级主要述求
         * 1：退役安置类
         * 2：优待抚恤类
         * 3：就业创业类
         * 4：褒扬纪念类
         * 5：军休服务类
         * 6：帮扶援助类
         * 7：党员管理类
         * 8：咨询建议类
         * 9：其他
         *
         * 传 数字
         */
        jsonObject.put("extMobileOne", "13662883374");
        jsonObject.put("regMainAppealOne", "9");
        jsonObject.put("extDomicileAddress", "北京市朝阳区大山子社区");//户籍地址
        jsonObject.put("acceptItem", "");//问题属地（行政区划码）这行为空
        jsonObject.put("regAppealContent", "主要述求详情");//主要述求详情
        //录音路径还有问题，需要注意
        jsonObject.put("regRecordFileUri","http://www.baidu.com/mp3/123.mp4");//录音文件地址
        System.out.println(jsonObject.toJSONString());


        String aaa=jsonObject.toJSONString();
        //String aaa="qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq";
        System.out.println(encrypt(aaa));

        System.out.println(decrypt(encrypt(aaa)));
    }
    /**
     * 加解密密钥, 外部可以
     */
    public static final String AES_DATA_SECURITY_KEY = "4%YkW!@g5LGcf9Ut";
    /**
     * 算法/加密模式/填充方式
     */
    private static final String AES_PKCS5P = "AES/ECB/PKCS5Padding";

    private static final String AES_PERSON_KEY_SECURITY_KEY = "pisnyMyZYXuCNcRd";

    /**
     * 加密
     *
     * @param str
     *            需要加密的字符串
     * @param key
     *            密钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String str, String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("key不能为空");
        }
        try {
            if (str == null) {
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                return null;
            }
            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(AES_PKCS5P);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return Base64.encodeBase64String(cipher.doFinal(str.getBytes("utf-8"))); //对数据加密后返回base64编码后的字符串
            //  byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            //  return new BASE64Encoder().encode(encrypted);
        } catch (Exception ex) {
            return null;
        }

    }

    /**
     * 解密
     *
     * @param str
     *            需要解密的字符串
     * @param key
     *            密钥
     * @return
     */
    public static String decrypt(String str, String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("key不能为空");
        }
        try {
            if (str == null) {
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                return null;
            }
            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(AES_PKCS5P);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] strencrypted = new BASE64Decoder().decodeBuffer(str);
            //System.out.println(new String(strencrypted, "UTF-8"));
            // 先用base64解密
            String aaa=str.replaceAll("\r\n", "");
            //System.out.println(aaa);
            byte[] encrypted = new BASE64Decoder().decodeBuffer(aaa);
            //System.out.println(new String(encrypted, "UTF-8"));
            try {
                byte[] original = cipher.doFinal(encrypted);
                String originalString = new String(original, "UTF-8");
                return originalString;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 加密
     *
     * @param str
     *            需要加密的字符串
     * @return
     * @throws Exception
     */
    public static String encrypt(String str) {
        return encrypt(str, AES_DATA_SECURITY_KEY);
    }

    /**
     * 解密
     *
     * @param str
     *            需要解密的字符串
     * @return
     */
    public static String decrypt(String str) {
        return decrypt(str, AES_DATA_SECURITY_KEY);
    }

    /**
     * 查询的时候对某些字段解密
     *
     * @param str
     * @return
     */
    public static String aesDecrypt(String str) {
        if (StringUtils.isEmpty(str)) {
            return " ";
        }
        String sql = " AES_DECRYPT(from_base64(" + str + ")," + "'" + AES_DATA_SECURITY_KEY + "')";
        return sql;
    }

    /**
     * 对personKey加密
     *
     * @param personKey
     * @return
     */
    public static String encryptPersonKey(String personKey) {
        return AESEncryptUtils.encrypt(personKey, AES_PERSON_KEY_SECURITY_KEY);
    }

    /**
     * 对personKey解密
     *
     * @param personKey
     * @return
     */
    public static String decryptPersonKey(String personKey) {
        return AESEncryptUtils.decrypt(personKey, AES_PERSON_KEY_SECURITY_KEY);
    }
}
