package cn.whitetown.esconfig.manager.wiml;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.esconfig.manager.EsSearchManager;
import cn.whitetown.esconfig.modo.EsIOException;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 检索操作
 * @author taixian
 * @date 2020/08/17
 **/
public class DefaultSearchManager implements EsSearchManager {

    private Logger log = LogConstants.SYS_LOGGER;

    @Autowired
    private RestHighLevelClient esClient;

    @Override
    public long queryCount(String indexName, SearchSourceBuilder sourceBuilder) {
        if(indexName == null || sourceBuilder == null) {
            return 0;
        }
        CountRequest request = new CountRequest(indexName);
        request.query(sourceBuilder.query());
        try {
            CountResponse count = esClient.count(request, RequestOptions.DEFAULT);
            return count.getCount();
        }catch (IOException e) {
            log.error(e.getMessage());
            throw new EsIOException(e.getMessage());
        }

    }

    @Override
    public SearchResponse query(String indexName, SearchSourceBuilder sourceBuilder) {
        if(indexName == null || sourceBuilder == null) {
            return null;
        }

        SearchRequest searchRequest = this.createSearchRequest(indexName, sourceBuilder);
        try {
            return esClient.search(searchRequest, RequestOptions.DEFAULT);
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new EsIOException(e.getMessage());
        }
    }

    @Override
    public SearchResponse pageQuery(String indexName, PageQuery pageQuery, SearchSourceBuilder sourceBuilder) {
        if(indexName == null || sourceBuilder == null) {
            return null;
        }
        pageQuery = WhiteToolUtil.defaultPage(pageQuery);
        sourceBuilder.from((pageQuery.getPage() - 1) * pageQuery.getSize())
                .size(pageQuery.getSize());
        return this.query(indexName,sourceBuilder);
    }

    /**
     * 构建search请求
     * @param indexName
     * @param sourceBuilder
     * @return
     */
    private SearchRequest createSearchRequest(String indexName,SearchSourceBuilder sourceBuilder) {
        SearchRequest request = new SearchRequest();
        request.indices(indexName);
        request.source(sourceBuilder);
        return request;
    }
}
