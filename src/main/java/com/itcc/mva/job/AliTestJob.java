package com.itcc.mva.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.HttpUtil;
import com.itcc.mva.entity.AliTestEntity;
import com.itcc.mva.mapper.AliTestMapper;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.util.*;

/**
 * @author whoami
 * 音频文件转码回调服务
 */
@Component
public class AliTestJob {

    public static List<String> getFileName() {

        List<String> name = new ArrayList<String>();
        String path = "Z:\\leaveword\\jt\\lnl\\"; // 路径
        File f = new File(path);//获取路径  F:\测试目录
        if (!f.exists()) {
            System.out.println(path + " not exists");//不存在就输出
            return name;
        }

        File fa[] = f.listFiles();//用数组接收  F:\笔记总结\C#, F:\笔记总结\if语句.txt
        for (int i = 0; i < fa.length; i++) {//循环遍历
            File fs = fa[i];//获取数组中的第i个
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");//如果是目录就输出
            } else {
                //System.out.println(fs.getName());//否则直接输出
                name.add(fs.getName());
            }
        }
        return name;
    }

    @Autowired
    private AliTestMapper aliTestMapper;

    @Scheduled(cron = "0/20 * * * * ?")
    @SchedulerLock(name = "AliTestVHm", lockAtMostFor = "18s", lockAtLeastFor = "18s")
    public void vocuPutRecordToAli() {
        List<String> name = getFileName();
        for(int i=0;i<name.size();i++) {
            if(aliTestMapper.selectCount(new QueryWrapper<AliTestEntity>().eq("FILENAME",name.get(i)))==0){
            AliTestEntity aliTestEntity = new AliTestEntity();
            aliTestEntity.setFilename(name.get(i));

            String audioUrl = "http://172.16.12.178:52111/lnl/" + name.get(i);
            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("Content-Type", "application/json");
            JSONObject sendAliweb = new JSONObject();
            sendAliweb.put(Constant.KEY_APPKEY, "default");
            sendAliweb.put(Constant.KEY_TOKEN, "default");
            sendAliweb.put(Constant.KEY_FILE_LINK, audioUrl);

            String responseOri = HttpUtil.httpPost(Constant.KEY_ALIASR, headers, sendAliweb.toJSONString(), null, Constant.HTTP_TIMEOUT, false);
            JSONObject JsonOri = JSONObject.parseObject(responseOri);
            if (JsonOri.containsKey("header") && Constant.STATUS_SUCCESS.equals(JsonOri.getJSONObject("header").getString("status_message"))) {
                String taskId = JsonOri.getJSONObject("header").getString("task_id");
                aliTestEntity.setOritaskid(taskId);
            }

            JSONObject sendVm = new JSONObject();
            sendVm.put(Constant.KEY_APPKEY, "default");
            sendVm.put(Constant.KEY_TOKEN, "default");
            sendVm.put(Constant.KEY_FILE_LINK, audioUrl);
            sendVm.put(Constant.CUSTOMIZATION_ID, "jt-model");

            String responseVm = HttpUtil.httpPost(Constant.KEY_ALIASR, headers, sendVm.toJSONString(), null, Constant.HTTP_TIMEOUT, false);
            JSONObject JsonVm = JSONObject.parseObject(responseVm);
            if (JsonVm.containsKey("header") && Constant.STATUS_SUCCESS.equals(JsonVm.getJSONObject("header").getString("status_message"))) {
                String taskVmId = JsonVm.getJSONObject("header").getString("task_id");
                aliTestEntity.setVmtaskid(taskVmId);
            }


                JSONObject sendVHm = new JSONObject();
                sendVHm.put(Constant.KEY_APPKEY, "default");
                sendVHm.put(Constant.KEY_TOKEN, "default");
                sendVHm.put(Constant.KEY_FILE_LINK, audioUrl);
                sendVHm.put(Constant.VOCABULARY_ID, "096d421ab4e74266b9ecd3c28e27e936");
                sendVHm.put(Constant.CUSTOMIZATION_ID, "jt-model");

                String responseHVm = HttpUtil.httpPost(Constant.KEY_ALIASR, headers, sendVHm.toJSONString(), null, Constant.HTTP_TIMEOUT, false);
                JSONObject JsonHVm = JSONObject.parseObject(responseHVm);
                if (JsonHVm.containsKey("header") && Constant.STATUS_SUCCESS.equals(JsonHVm.getJSONObject("header").getString("status_message"))) {
                    String taskVHmId = JsonHVm.getJSONObject("header").getString("task_id");
                    aliTestEntity.setVhmtaskid(taskVHmId);
                }
                aliTestMapper.insert(aliTestEntity);
            }
        }
    }


    @Scheduled(cron = "0/20 * * * * ?")
    @SchedulerLock(name = "AliTestGetVHmResult", lockAtMostFor = "18s", lockAtLeastFor = "18s")
    public void AliTestGetVHmResult() throws IOException {

        List<AliTestEntity> aliTestEntities=aliTestMapper.selectList(new QueryWrapper<AliTestEntity>().isNotNull("ORITASKID").isNotNull("VMTASKID").isNotNull("VHMTASKID"));

        for(int j=0;j<aliTestEntities.size();j++){



            AliTestEntity aliTestEntity = new AliTestEntity();
            if(aliTestMapper.selectOne(new QueryWrapper<AliTestEntity>().eq("ORITASKID",aliTestEntities.get(j).getOritaskid())).getOriResult()==null){

            StringBuffer oriResult = new StringBuffer();
            Map<String, Object> requestOri = new HashMap<String, Object>();
            requestOri.put(Constant.KEY_TASK_ID, aliTestEntities.get(j).getOritaskid());
            String ori = HttpUtil.get(Constant.KEY_QUERYALIASR, requestOri);
            if (null != ori) {
                JSONObject oriJson = JSONObject.parseObject(ori);
                if (oriJson.containsKey("header") && oriJson.containsKey("payload")) {
                    switch (oriJson.getJSONObject("header").getString("status_message")) {
                        case Constant.STATUS_SUCCESS:
                            JSONObject payloadOri = oriJson.getJSONObject("payload");
                            if (payloadOri.containsKey("sentences")) {
                                for (int i = 0; i < payloadOri.getJSONArray("sentences").size(); i++) {
                                    oriResult.append(((JSONObject) payloadOri.getJSONArray("sentences").get(i)).get("text")).append("\r\n");
                                }
                                aliTestEntity.setOriResult(oriResult.toString());

                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            }

            if(aliTestMapper.selectOne(new QueryWrapper<AliTestEntity>().eq("VMTASKID",aliTestEntities.get(j).getVmtaskid())).getVmResult()==null) {

                StringBuffer vmResult = new StringBuffer();
                Map<String, Object> requestVm = new HashMap<String, Object>();
                requestVm.put(Constant.KEY_TASK_ID, aliTestEntities.get(j).getVmtaskid());
                String vm = HttpUtil.get(Constant.KEY_QUERYALIASR, requestVm);
                if (null != vm) {
                    JSONObject vmJson = JSONObject.parseObject(vm);
                    if (vmJson.containsKey("header") && vmJson.containsKey("payload")) {
                        switch (vmJson.getJSONObject("header").getString("status_message")) {
                            case Constant.STATUS_SUCCESS:
                                JSONObject payloadVm = vmJson.getJSONObject("payload");
                                if (payloadVm.containsKey("sentences")) {
                                    for (int i = 0; i < payloadVm.getJSONArray("sentences").size(); i++) {
                                        vmResult.append(((JSONObject) payloadVm.getJSONArray("sentences").get(i)).get("text")).append("\r\n");
                                    }
                                    aliTestEntity.setVmResult(vmResult.toString());
                                    //aliTestMapper.update(aliTestEntity,new QueryWrapper<AliTestEntity>().eq("VMTASKID",aliTestEntities.get(j).getVmtaskid()));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            if(aliTestMapper.selectOne(new QueryWrapper<AliTestEntity>().eq("VHMTASKID",aliTestEntities.get(j).getVhmtaskid())).getVhmResult()==null) {

                StringBuffer vhmResult = new StringBuffer();
                Map<String, Object> requestVhm = new HashMap<String, Object>();
                requestVhm.put(Constant.KEY_TASK_ID, aliTestEntities.get(j).getVhmtaskid());
                String vhm = HttpUtil.get(Constant.KEY_QUERYALIASR, requestVhm);
                if (null != vhm) {
                    JSONObject vhmJson = JSONObject.parseObject(vhm);
                    if (vhmJson.containsKey("header") && vhmJson.containsKey("payload")) {
                        switch (vhmJson.getJSONObject("header").getString("status_message")) {
                            case Constant.STATUS_SUCCESS:
                                JSONObject payloadVhm = vhmJson.getJSONObject("payload");
                                if (payloadVhm.containsKey("sentences")) {
                                    for (int i = 0; i < payloadVhm.getJSONArray("sentences").size(); i++) {
                                        vhmResult.append(((JSONObject) payloadVhm.getJSONArray("sentences").get(i)).get("text")).append("\r\n");
                                    }
                                    aliTestEntity.setVhmResult(vhmResult.toString());
                                    //aliTestMapper.update(aliTestEntity,new QueryWrapper<AliTestEntity>().eq("VHMTASKID",aliTestEntities.get(j).getVmtaskid()));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            aliTestMapper.update(aliTestEntity,new QueryWrapper<AliTestEntity>().eq("ORITASKID",aliTestEntities.get(j).getOritaskid()));
            }
    }


}