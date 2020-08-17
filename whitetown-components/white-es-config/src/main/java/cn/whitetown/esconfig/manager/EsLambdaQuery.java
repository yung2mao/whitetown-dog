package cn.whitetown.esconfig.manager;


import cn.whitetown.dogbase.common.util.WhiteFunc;
import cn.whitetown.esconfig.config.EsConfigEnum;
import cn.whitetown.esconfig.modo.EsRange;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

/**
 * 自定义QueryBuilder构造器
 * @author taixian
 * @date 2020/08/17
 **/
public interface EsLambdaQuery {

    /**
     * and查询 - must
     * @return
     */
    EsLambdaQuery and();

    /**
     * or查询 - should
     * @return
     */
    EsLambdaQuery or();

    /**
     * not - 条件不允许满足
     * @return
     */
    EsLambdaQuery not();

    /**
     * 获取最终用户搜索的searchSource
     * @return
     */
    SearchSourceBuilder getQueryBuilder();

    /**
     * 查询所有的条件
     * @return
     */
    QueryBuilder queryAllBuilder();

    /**
     * equal
     * @param column
     * @param value
     * @param <R>
     * @return
     */
    <R> EsLambdaQuery eq(WhiteFunc<R,Object> column, Object value);

    /**
     * equal
     * @param column
     * @param value
     * @param configEnum
     * @param <R>
     * @return
     */
    <R> EsLambdaQuery match(WhiteFunc<R,Object> column, Object value, EsConfigEnum configEnum);

    /**
     * 平均值
     * @param column
     * @return
     */
    <R> SearchSourceBuilder avg(WhiteFunc<R,Object> column);

    /**
     * 最大值
     * @param column
     * @param <R>
     * @return
     */
    <R> SearchSourceBuilder max(WhiteFunc<R,Object> column);

    /**
     * 最小值
     * @param column
     * @param <R>
     * @return
     */
    <R> SearchSourceBuilder min(WhiteFunc<R,Object> column);

    /**
     * 范围聚合
     * @param column
     * @param
     * @param <R>
     * @return
     */
    <R> SearchSourceBuilder range(WhiteFunc<R,Object> column, List<EsRange> ranges);

}
