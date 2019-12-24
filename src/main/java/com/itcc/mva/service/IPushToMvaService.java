package com.itcc.mva.service;

import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.entity.QuarkCallbackEntity;

public interface IPushToMvaService {
    void singleSendToMvaServiceJt(IntelligentAsrEntity intelligentAsrEntity);
    void singleSendToMvaServiceKd(QuarkCallbackEntity quarkCallbackEntity);
    void singleSendToMvaServiceAl();

    String illegalId(String id);
}
