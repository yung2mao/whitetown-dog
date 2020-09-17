package cn.whitetown.mshow.service.wiml;

import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.esconfig.manager.EsSearchManager;
import cn.whitetown.mshow.modo.ao.LogDetailQuery;
import cn.whitetown.mshow.service.LogBaseService;
import cn.whitetown.mshow.service.basese.LogDeHandlerEnum;
import cn.whitetown.mshow.service.basese.LogDetailHandler;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.constraints.NotNull;
import java.util.List;

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
        LogDetailHandler logDetailHandler = LogDeHandlerEnum.logDetailHandler(logType);
        SearchSourceBuilder searchBuilder = logDetailHandler.getSearchBuilder(condition);
        SearchResponse response = esSearchManager.pageQuery(null, condition, searchBuilder);
        List<Object> result = logDetailHandler.result2Obj(response.getHits());
        return null;
    }
}
