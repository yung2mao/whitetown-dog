package cn.whitetown.mshow.service;

import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.mshow.modo.ao.LogDetailQuery;

/**
 * @author taixian
 * @date 2020/09/15
 **/
public interface LogBaseService {
    /**
     * 分页检索日志详情信息
     * @param condition
     * @return
     */
    ResponsePage queryLogPage(LogDetailQuery condition);
}
