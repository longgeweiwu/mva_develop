package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.Tools;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IflyAsrJob {

    public void fileTest() {
        String wavcid = UUID.randomUUID().toString().replaceAll("-","");
        //生成唯一的消息通知地址
        String task_notyfy_url= Constant.NOTIFYURL+"/"+wavcid;
        //添加任务
        Tools.addTask(wavcid, Constant.URL,Constant.AUDIO,task_notyfy_url);
    }

}
