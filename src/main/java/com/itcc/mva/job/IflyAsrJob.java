package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.service.IQuarkCallbackService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IflyAsrJob {

    @Autowired
    private IQuarkCallbackService iQuarkCallbackService;

    //@Scheduled(cron = "* 0/2 * * * ?")
    //@SchedulerLock(name = "PushToIflyAudioJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void pushToIflyAudio() {
        String wavcid = UUID.randomUUID().toString().replaceAll("-","");
        //生成唯一的消息通知地址
        String task_notyfy_url= Constant.NOTIFYURL+"/"+wavcid;
        //添加任务
        Tools.addTask(wavcid, Constant.URL,Constant.AUDIO,task_notyfy_url);
    }

}
