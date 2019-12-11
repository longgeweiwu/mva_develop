package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.service.IAsrJsonParseService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 解析JSON 任务JOB
 * @author whoami
 */
@Component
public class AsrJsonParseJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IAsrJsonParseService iAsrJsonParseService;

   // @Scheduled(cron = "* 0/2 * * * ?")
//    @SchedulerLock(name = "AsrJsonParseJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void runParse()
    {
        List<IntelligentAsrEntity> asrEntityList = iAsrJsonParseService.queryPendingTop(Constant.NO_PARSER);
        for (int i = 0; i < asrEntityList.size(); i++) {
            iAsrJsonParseService.jsonSigle(asrEntityList.get(i));
        }
    }
}
