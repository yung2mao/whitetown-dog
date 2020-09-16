package cn.whitetown.mshow.service.basese;

import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.esconfig.manager.wiml.LambdaEsQueryImpl;
import cn.whitetown.esconfig.modo.EsRange;
import cn.whitetown.mshow.modo.ao.LogDetailQuery;
import cn.whitetown.mshow.modo.log.SystemLog;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author taixian
 * @date 2020/09/16
 **/
public class SysLogQueryHandler implements LogDetailHandler {
    @Override
    public SearchSourceBuilder getSearchBuilder(LogDetailQuery condition) {
        List<EsRange> ranges = new ArrayList<>();
        double startTime = WhiteFormatUtil.timeAsLong(WhiteFormatUtil.text2Date(condition.getStartTime()));
        double endTime = WhiteFormatUtil.timeAsLong(WhiteFormatUtil.text2Date(condition.getEndTime()));
        ranges.add(new EsRange(null, startTime,endTime));
        return LambdaEsQueryImpl.getLambdaQuery().range(SystemLog::getLogTime,ranges);
    }

    @Override
    public <T> List<T> result2Obj(SearchHits hits) {
        return null;
    }
}
