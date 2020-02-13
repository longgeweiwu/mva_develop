package com.itcc.mva.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.entity.TxAsrEntity;
import com.itcc.mva.mapper.TxMapper;
import com.itcc.mva.service.ITxService;
import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskRequest;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskResponse;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusRequest;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Service
public class TxServiceImpl implements ITxService {

    @Autowired
    private TxMapper txMapper;


    @Override
    public void generateTxBaseTable() {
        txMapper.generateTxBaseTable();
    }

    @Override
    public List<TxAsrEntity> queryTxFileTop(int top) {
        return txMapper.queryTxFileTop(top);
    }

    @Override
    public void uploadTxFile(TxAsrEntity txAsrEntity) {
        String fileName = txAsrEntity.getLeaveWordpath() + txAsrEntity.getVoiceFileName();
        Map<String, Object> map = new HashMap<>();
        map.put("filePath", Constant.TX_FILEPATH + txAsrEntity.getVoiceFileName().substring(0,8) +"/");
        try {
            uploadFile(fileName, map);
            TxAsrEntity asrEntity = new TxAsrEntity();
            asrEntity.setTxfileflag(Constant.UPFILE_SUCCESS);
            txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
        } catch (Exception e) {
            e.printStackTrace();
            TxAsrEntity asrEntity = new TxAsrEntity();
            asrEntity.setTxfileflag(Constant.UPFILE_FAIL);
            txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
        }
    }

    @Override
    public List<TxAsrEntity> queryTxPendingTop(int top) {
        return txMapper.queryTxPendingTop(top);
    }

    @Override
    public void addTxTask(TxAsrEntity txAsrEntity) {
        try {

            Credential cred = new Credential(Constant.SECRETID,Constant.SECRETKEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.ap-beijing.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            AsrClient client = new AsrClient(cred, "ap-beijing", clientProfile);

            String params = "{\"EngineModelType\":\"8k_zh_s\",\"ChannelNum\":1,\"ResTextFormat\":0,\"SourceType\":0,\"Url\":\"" +
                    Constant.TX_AUDIL_URL+txAsrEntity.getVoiceFileName().substring(0,8)+"/"+txAsrEntity.getVoiceFileName()+
                    "\",\"HotwordId\":\""+Constant.TX_HOTWORDID+"\"}";
            CreateRecTaskRequest req = CreateRecTaskRequest.fromJsonString(params, CreateRecTaskRequest.class);

            CreateRecTaskResponse resp = client.CreateRecTask(req);

            System.out.println(CreateRecTaskRequest.toJsonString(resp));

            JSONObject result = JSONObject.parseObject(CreateRecTaskRequest.toJsonString(resp));

            if(result.containsKey("Data") && result.getJSONObject("Data").containsKey("TaskId")){
                TxAsrEntity asrEntity = new TxAsrEntity();
                asrEntity.setAsrflag(Constant.ASRPARSER_TX_SUCCESS);
                asrEntity.setTaskid(result.getJSONObject("Data").getString("TaskId"));
                txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
            }else{
                TxAsrEntity asrEntity = new TxAsrEntity();
                asrEntity.setAsrflag(Constant.ASRRESULT_TX_FAIL);
                txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            TxAsrEntity asrEntity = new TxAsrEntity();
            asrEntity.setAsrflag(Constant.ASRPARSER_TX_FAIL);
            txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
        }
    }

    @Override
    public List<TxAsrEntity> queryTxResultTop(int top) {
        return txMapper.queryTxResultTop(top);
    }

    @Override
    public void queryAndSetTxBase(TxAsrEntity txAsrEntity) {
        try{

            Credential cred = new Credential(Constant.SECRETID,Constant.SECRETKEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.ap-beijing.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            AsrClient client = new AsrClient(cred, "ap-beijing", clientProfile);

            String params = "{\"TaskId\":"+txAsrEntity.getTaskid()+"}";//623390897//623385178//623394120
            DescribeTaskStatusRequest req = DescribeTaskStatusRequest.fromJsonString(params, DescribeTaskStatusRequest.class);

            DescribeTaskStatusResponse resp = client.DescribeTaskStatus(req);

            System.out.println(DescribeTaskStatusRequest.toJsonString(resp));

            JSONObject result = JSONObject.parseObject(DescribeTaskStatusRequest.toJsonString(resp));

            if(result.containsKey("Data") && result.getJSONObject("Data").containsKey("Status")){
                if(2 == result.getJSONObject("Data").getIntValue("Status")){
                    TxAsrEntity asrEntity = new TxAsrEntity();
                    asrEntity.setInsertTime(new Date());
                    asrEntity.setTxparseStatus(Constant.ASRJSONRESULT_TX_SUCCESS);
                    asrEntity.setTxResult(result.getJSONObject("Data").getString("Result").replaceAll("\\[.*\\]", "").replaceAll("\n", "").replaceAll(" ", ""));
                    txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
                }else if(3 == result.getJSONObject("Data").getIntValue("Status")){
                    TxAsrEntity asrEntity = new TxAsrEntity();
                    asrEntity.setInsertTime(new Date());
                    asrEntity.setTxparseStatus(Constant.ASRJSONRESULT_TX_NOFAIL);
                    txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
                }else{
                    //其他情况 0 任务等待 1任务执行 这种情况需要在次查询 3是失败 2是成功
                }
            }else{
                TxAsrEntity asrEntity = new TxAsrEntity();
                asrEntity.setInsertTime(new Date());
                asrEntity.setTxparseStatus(Constant.ASRJSONRESULT_TX_OTHERFAIL);
                txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            TxAsrEntity asrEntity = new TxAsrEntity();
            asrEntity.setInsertTime(new Date());
            asrEntity.setTxparseStatus(Constant.ASRJSONRESULT_TX_FAIL);
            txMapper.update(asrEntity,new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
        }
    }

    public static void uploadFile(String fileName, Map<String, Object> map) throws Exception {
        // 换行符
        final String newLine = "\r\n";
        final String boundaryPrefix = "--";
        // 定义数据分隔线
        String BOUNDARY = "========7d4a6d158c9";
        // 服务器的域名
        URL url = new URL(Constant.TX_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置为POST情
        conn.setRequestMethod("POST");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 设置请求头参数
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        try (
                OutputStream outputStream = conn.getOutputStream();
                DataOutputStream out = new DataOutputStream(outputStream);
        ) {
            //传递参数

            if (map != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    stringBuilder.append(boundaryPrefix)
                            .append(BOUNDARY)
                            .append(newLine)
                            .append("Content-Disposition: form-data; name=\"")
                            .append(entry.getKey())
                            .append("\"").append(newLine).append(newLine)
                            .append(String.valueOf(entry.getValue()))
                            .append(newLine);
                }
                out.write(stringBuilder.toString().getBytes(Charset.forName("UTF-8")));
            }

            // 上传文件
            {
                File file = new File(fileName);
                StringBuilder sb = new StringBuilder();
                sb.append(boundaryPrefix);
                sb.append(BOUNDARY);
                sb.append(newLine);
                sb.append("Content-Disposition: form-data;name=\"file\";filename=\"").append(file.getName())
                        .append("\"").append(newLine);
                sb.append("Content-Type:application/octet-stream");
                sb.append(newLine);
                sb.append(newLine);
                System.out.println(sb.toString());
                out.write(sb.toString().getBytes());

                try (
                        DataInputStream in = new DataInputStream(new FileInputStream(file));
                ) {
                    byte[] bufferOut = new byte[1024];
                    int bytes = 0;
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    out.write(newLine.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //定义BufferedReader输入流来读取URL的响应
        try (
                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
        ) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
