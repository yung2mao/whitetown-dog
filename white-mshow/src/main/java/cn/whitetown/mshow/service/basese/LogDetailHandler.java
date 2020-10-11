package cn.whitetown.mshow.service.basese;

import cn.whitetown.mshow.modo.ao.LogDetailQuery;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

/**
 * @author taixian
 * @date 2020/09/16
 **/
public interface LogDetailHandler<T> {
    /**
     * 获取elasticsearch检索项
     * @param condition
     * @return
     */
    SearchSourceBuilder getSearchBuilder(LogDetailQuery condition);

    /**
     * 结果集转为丢向List
     * @param hits
     * @param <T>
     * @return
     */
    List<T> result2Obj(SearchHits hits);
}
