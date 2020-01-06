package com.itcc.mva.mapper;

import com.itcc.mva.vo.MvaOutVo;
/**
 * @author whoami
 */
public interface PushToMvaMapper {

    /**
     * 通过calli查询拼的信息
     * @param callid
     * @return
     */
    MvaOutVo queryByCallid(String callid);
}
