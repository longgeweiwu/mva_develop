package com.itcc.mva.mapper;

import com.itcc.mva.vo.MvaOutVo;

public interface PushToMvaMapper {

    /**
     * 通过calli查询拼的信息
     * @param callid
     * @return
     */
    MvaOutVo queryByCallid(String callid);
}
