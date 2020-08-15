package cn.whitetown.esconfig.manager;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * 搜索操作
 * @author taixian
 * @date 2020/08/14
 **/
public interface EsSearchManager {
    /**
     * 查询满足条件的文档数量
     * @param indexName
     * @param queryBuilder
     * @return
     */
    long queryCount(String indexName, QueryBuilder queryBuilder);

    /**
     * 条件检索数据
     * @param indexName
     * @param queryBuilder
     * @return
     */
    SearchResponse query(String indexName, QueryBuilder queryBuilder);

    /**
     * 分页查询
     * @param indexName
     * @param pageQuery
     * @param queryBuilder
     * @return
     */
    SearchResponse pageQuery(String indexName, PageQuery pageQuery, QueryBuilder queryBuilder);

    /**
     * 多查询条件 - 同时满足
     * @param queryBuilders
     * @return
     */
    QueryBuilder mustQuery(QueryBuilder ... queryBuilders);

}
