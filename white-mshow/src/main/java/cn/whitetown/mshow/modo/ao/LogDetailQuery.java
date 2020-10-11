package cn.whitetown.mshow.modo.ao;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import cn.whitetown.logbase.config.LogConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 日志详情查询条件
 * @author taixian
 * @date 2020/09/15
 **/
@Getter
@Setter
public class LogDetailQuery extends PageQuery {

    @NotNull
    private String logType = LogConstants.SYS_LOG;
}
