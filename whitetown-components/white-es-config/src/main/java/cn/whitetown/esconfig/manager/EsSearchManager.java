package cn.whitetown.esconfig.manager;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * 搜索操作
 * @author taixian
 * @date 2020/08/14
 **/
public interface EsSearchManager {
    /**
     * 查询满足条件的文档数量
     * @param indexName
     * @param sourceBuilder
     * @return
     */
    long queryCount(String indexName, SearchSourceBuilder sourceBuilder);

    /**
     * 条件检索数据
     * @param indexName
     * @param sourceBuilder
     * @return
     */
    SearchResponse query(String indexName, SearchSourceBuilder sourceBuilder);

    /**
     * 分页查询
     * @param indexName
     * @param pageQuery
     * @param sourceBuilder
     * @return
     */
    SearchResponse pageQuery(String indexName, PageQuery pageQuery, SearchSourceBuilder sourceBuilder);

}
