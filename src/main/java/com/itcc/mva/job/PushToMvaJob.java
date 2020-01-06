package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.entity.AliAsrEntity;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.entity.QuarkCallbackEntity;
import com.itcc.mva.service.IAsrJsonParseService;
import com.itcc.mva.service.IPushToMvaService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author whoami
 */
@Component
public class PushToMvaJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${engineType.jttype}")
    private int jttype;

    @Value("${engineType.kdtype}")
    private int kdtype;

    @Value("${engineType.altype}")
    private int altype;

    @Autowired
    private IAsrJsonParseService iAsrJsonParseService;

    @Autowired
    private IPushToMvaService iPushToMvaService;

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "MvaPushToJob", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void pushInfo() {
        long start_PushToMvaJob = System.currentTimeMillis();
        logger.info(jttype == 1 ? "[>>> JT引擎 开启状态]" : "[>>> JT引擎 关闭状态]" + "  " + (kdtype == 1 ? "[>>> KD引擎 开启状态]" : "[>>> KD引擎 关闭状态]") + "  " + (altype == 1 ? "[>>> AL引擎 开启状态]" : "[>>> AL引擎 关闭状态]"));
        if (jttype == Constant.ENGINETYPE_JT) {
            logger.info(">>> 正在使用JT引擎");
            List<IntelligentAsrEntity> waitingSend = iAsrJsonParseService.queryWaitingSendJt(Constant.NO_SENDER);
            if (0 != waitingSend.size()) {
                logger.info(">>> 存在[JT推送]任务 。 开始时间 [" + new Date() + "]");
                for (int i = 0; i < waitingSend.size(); i++) {
                    iPushToMvaService.singleSendToMvaServiceJt(waitingSend.get(i));
                }
                logger.info(">>> 存在[JT推送]任务 。 结束时间 [" + new Date() + "]");
            } else {
                logger.info(">>> 任务名称:MvaPushToJob 暂时没有[JT推送]任务。");
            }
        }
        if (kdtype == Constant.ENGINETYPE_KD) {
            logger.info(">>> 正在使用KD引擎");
            List<QuarkCallbackEntity> waitingSend = iAsrJsonParseService.queryWaitingSendKd(Constant.NO_SENDER);
            if (0 != waitingSend.size()) {
                logger.info(">>> 存在[KD推送]任务 。 开始时间 [" + new Date() + "]");
                for (int i = 0; i < waitingSend.size(); i++) {
                    iPushToMvaService.singleSendToMvaServiceKd(waitingSend.get(i));
                }
                logger.info(">>> 存在[KD推送]任务 。 结束时间 [" + new Date() + "]");
            } else {
                logger.info(">>> 任务名称:MvaPushToJob 暂时没有[KD推送]任务。");
            }
        }
        if (altype == Constant.ENGINETYPE_AL) {
            logger.info(">>> 正在使用阿里引擎");
            List<AliAsrEntity> waitingSend = iAsrJsonParseService.queryWaitingSendAl(Constant.NO_SENDER);
            if (0 != waitingSend.size()) {
                logger.info(">>> 存在[AL推送]任务 。 开始时间 [" + new Date() + "]");
                for (int i = 0; i < waitingSend.size(); i++) {
                    iPushToMvaService.singleSendToMvaServiceAl(waitingSend.get(i));
                }
                logger.info(">>> 存在[AL推送]任务 。 结束时间 [" + new Date() + "]");
            } else {
                logger.info(">>> 任务名称:MvaPushToJob 暂时没有[AL推送]任务。");
            }
        }
        long end_PushToMvaJob = System.currentTimeMillis() - start_PushToMvaJob;
        logger.info(">>> 任务名称:MvaPushToJob(推部委接口) 总执行时间为: [" + end_PushToMvaJob + " ms]");
    }
}
