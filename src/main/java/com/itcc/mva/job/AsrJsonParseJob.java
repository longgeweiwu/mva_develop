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

import java.util.Date;
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

//    @Scheduled(cron = "* 0/2 * * * ?")
//    @SchedulerLock(name = "JtAsrParseResultJob", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void runParse() {
        long startRunParse = System.currentTimeMillis();
        List<IntelligentAsrEntity> asrEntityList = iAsrJsonParseService.queryPendingTop(Constant.NO_PARSER);
        if (0 != asrEntityList.size()) {
            logger.info(">>> 存在[解析]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iAsrJsonParseService.jsonSigle(asrEntityList.get(i));
            }
            logger.info(">>> 存在[解析]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:JtAsrParseResultJob 暂时没有[解析]任务。");
        }
        long endRunParse = System.currentTimeMillis() - startRunParse;
        logger.info(">>> 任务名称:JtAsrParseResultJob(捷通JSON文件解析) 总执行时间为: [" + endRunParse + " ms]");
    }
}
