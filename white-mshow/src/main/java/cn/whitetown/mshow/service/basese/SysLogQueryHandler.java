package cn.whitetown.mshow.service.basese;

import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.esconfig.manager.wiml.LambdaEsQueryImpl;
import cn.whitetown.esconfig.modo.EsRange;
import cn.whitetown.mshow.modo.ao.LogDetailQuery;
import cn.whitetown.mshow.modo.log.SystemLog;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author taixian
 * @date 2020/09/16
 **/
public class SysLogQueryHandler implements LogDetailHandler<SystemLog> {
    @Override
    public SearchSourceBuilder getSearchBuilder(LogDetailQuery condition) {
        List<EsRange> ranges = new ArrayList<>();
        double startTime = WhiteFormatUtil.timeAsLong(WhiteFormatUtil.text2Date(condition.getStartTime()));
        double endTime = WhiteFormatUtil.timeAsLong(WhiteFormatUtil.text2Date(condition.getEndTime()));
        ranges.add(new EsRange(null, startTime,endTime));
        return LambdaEsQueryImpl.getLambdaQuery().range(SystemLog::getLogTime,ranges);
    }

    @Override
    public List<SystemLog> result2Obj(SearchHits hits) {
        List<SystemLog> logs = new ArrayList<>();
        Arrays.stream(hits.getHits()).forEach(searchHit->{
            String source = searchHit.getSourceAsString();
            logs.add(JSON.parseObject(source,SystemLog.class));
        });
        return logs;
    }
}
