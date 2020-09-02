package cn.whitetown.esconfig.manager.wiml;

import cn.whitetown.dogbase.common.util.WhiteFunc;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.esconfig.config.EsConfigEnum;
import cn.whitetown.esconfig.manager.EsLambdaQuery;
import cn.whitetown.esconfig.modo.EsRange;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MinAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

/**
 * 基于lambda的es查询条件构造器
 * 禁止不同线程复用
 * @author taixian
 * @date 2020/08/17
 **/
public class LambdaEsQueryImpl implements EsLambdaQuery {

    private BoolQueryBuilder queryBuilder;

    private SearchSourceBuilder searchSourceBuilder;

    private QueryBool bool = QueryBool.MUST;

    private LambdaEsQueryImpl() {
        searchSourceBuilder = new SearchSourceBuilder();
    }

    public static EsLambdaQuery getLambdaQuery() {
        return new LambdaEsQueryImpl();
    }

    @Override
    public EsLambdaQuery and() {
        this.bool = QueryBool.MUST;
        return this;
    }

    @Override
    public EsLambdaQuery or() {
        this.bool = QueryBool.SHOULD;
        return this;
    }

    @Override
    public EsLambdaQuery not() {
        this.bool = QueryBool.MUST_NOT;
        return this;
    }


    @Override
    public SearchSourceBuilder getQueryBuilder() {
        if(queryBuilder != null) {
            searchSourceBuilder.query(this.queryBuilder);
        }
        return searchSourceBuilder;
    }

    @Override
    public QueryBuilder queryAllBuilder() {
        return QueryBuilders.matchAllQuery();
    }

    @Override
    public <R> EsLambdaQuery eq(WhiteFunc<R,Object> column, Object value) {
        String fieldName = WhiteToolUtil.getFieldName(column);
        TermQueryBuilder termQuery = QueryBuilders.termQuery(fieldName, value);
        this.addCondition(termQuery);
        return this;
    }

    @Override
    public <R> EsLambdaQuery match(WhiteFunc<R, Object> column, Object value, EsConfigEnum configEnum) {
        String fieldName = WhiteToolUtil.getFieldName(column);
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery(fieldName, String.valueOf(value));
        if(configEnum == EsConfigEnum.ES_IK || configEnum == EsConfigEnum.ES_STANDARD) {
            matchQuery.analyzer(configEnum.getValue());
        }
        this.addCondition(matchQuery);
        return this;
    }

    @Override
    public <R> SearchSourceBuilder avg(WhiteFunc<R, Object> column) {
        String fieldName = WhiteToolUtil.getFieldName(column);
        AvgAggregationBuilder avg = AggregationBuilders.avg(fieldName);
        return this.getQueryBuilder().aggregation(avg);
    }

    @Override
    public <R> SearchSourceBuilder max(WhiteFunc<R, Object> column) {
        String fieldName = WhiteToolUtil.getFieldName(column);
        MaxAggregationBuilder max = AggregationBuilders.max(fieldName);
        return this.getQueryBuilder().aggregation(max);
    }

    @Override
    public <R> SearchSourceBuilder min(WhiteFunc<R, Object> column) {
        String fieldName = WhiteToolUtil.getFieldName(column);
        MinAggregationBuilder min = AggregationBuilders.min(fieldName);
        return this.getQueryBuilder().aggregation(min);
    }

    @Override
    public <R> SearchSourceBuilder range(WhiteFunc<R, Object> column, List<EsRange> ranges) {
        String fieldName = WhiteToolUtil.getFieldName(column);
        RangeAggregationBuilder range = AggregationBuilders.range(fieldName);
        if(ranges.size() == 0) {
            range.addRange(Double.MIN_VALUE,Double.MAX_VALUE);
            return this.getQueryBuilder().aggregation(range);
        }
        ranges.stream().forEach(r -> {
            String key = r.getKey();
            Double from = r.getFrom();
            Double to = r.getTo();
            if(key == null) {
                if(from != null && to == null) {
                    range.addUnboundedFrom(from);
                }else if (to != null && from == null) {
                    range.addUnboundedTo(to);
                }else if (from != null && to != null) {
                    range.addRange(from,to);
                }
            } else {
                if(from != null && to == null) {
                    range.addUnboundedFrom(key,from);
                }else if (to != null && from == null) {
                    range.addUnboundedTo(key,to);
                }else if (from != null && to != null) {
                    range.addRange(key,from,to);
                }
            }
        });
        return this.getQueryBuilder().aggregation(range);
    }

    /**
     * 添加must / should / must_not条件
     * @param queryCondition
     */
    private void addCondition(QueryBuilder queryCondition) {
        if(queryBuilder == null) {
            queryBuilder = QueryBuilders.boolQuery();
        }

        if(bool == QueryBool.MUST) {
            queryBuilder.must(queryCondition);
        }else if (bool == QueryBool.SHOULD) {
            queryBuilder.should(queryCondition);
        }else {
            queryBuilder.mustNot(queryCondition);
        }
        bool = QueryBool.MUST;
    }

    /*-------------inner class-----------------*/

    static enum QueryBool {
        MUST,SHOULD,MUST_NOT;
    }

}
