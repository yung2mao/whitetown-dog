package cn.whitetown.mshow.service.wiml;

import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.esconfig.manager.EsSearchManager;
import cn.whitetown.mshow.modo.ao.LogDetailQuery;
import cn.whitetown.mshow.service.LogBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * @author taixian
 * @date 2020/09/15
 **/
@Service
public class LogBaseServiceImpl implements LogBaseService {

    @Autowired
    private EsSearchManager esSearchManager;

    @Override
    public ResponsePage queryLogPage(LogDetailQuery condition) {
        @NotNull String logType = condition.getLogType();

        return null;
    }
}
