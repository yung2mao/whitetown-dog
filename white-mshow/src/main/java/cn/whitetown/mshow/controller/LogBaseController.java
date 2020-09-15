package cn.whitetown.mshow.controller;

import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.mshow.modo.ao.LogDetailQuery;
import cn.whitetown.mshow.service.LogBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志基本展示
 * @author taixian
 * @date 2020/08/18
 **/
@RestController
@RequestMapping("log_base")
public class LogBaseController {

    @Autowired
    private LogBaseService logBaseService;

    /**
     * 分页查询日志详情信息
     * @param condition
     * @return
     */
    @GetMapping("/page")
    public ResponseData<ResponsePage> queryLogDetail(LogDetailQuery condition) {
        WhiteToolUtil.defaultPage(condition);
        ResponsePage result = logBaseService.queryLogPage(condition);
        return ResponseData.ok(result);
    }

}
