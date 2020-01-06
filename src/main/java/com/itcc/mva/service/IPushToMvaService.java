package com.itcc.mva.service;

import com.itcc.mva.entity.AliAsrEntity;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.entity.QuarkCallbackEntity;
/**
 * @author whoami
 */
public interface IPushToMvaService {
    void singleSendToMvaServiceJt(IntelligentAsrEntity intelligentAsrEntity);
    void singleSendToMvaServiceKd(QuarkCallbackEntity quarkCallbackEntity);
    void singleSendToMvaServiceAl(AliAsrEntity aliAsrEntity);

    String illegalId(String id);
}
