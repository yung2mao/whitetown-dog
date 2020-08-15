package cn.whitetown.esconfig.manager;

import java.io.IOException;
import java.util.Map;

/**
 * 索引操作
 * @author taixian
 * @date 2020/08/14
 **/
public interface EsIndicesManager {

    /**
     * 根据indexName创建索引
     * @param indexName
     * @return
     */
    boolean createIndex(String indexName);

    /**
     * 根据实体类创建索引
     * 默认索引名称等于类名称小写形式
     * @param entity
     * @param <T>
     * @return
     */
    <T> boolean createIndex(T entity);

    /**
     * 根据索引名称, 任意entity创建索引
     * 根据entity中字段类型和初始值创建默认类型的mapping
     * @param indexName 索引名称
     * @param entity 索引mapping对应的实体类
     * @param <T>
     * @return
     */
    <T> boolean createIndex(String indexName,T entity);

    /**
     * 根据索引名称,创建具有静态mapping的索引
     * @param indexName
     * @param fieldsMap
     * @return
     */
    boolean createIndexByMapping(String indexName, Map<String, Map<String,String>> fieldsMap);

    /**
     * 删除索引
     * @param indices
     * @return
     */
    boolean removeIndices(String ... indices);

    /**
     * 索引信息是否存在
     * @param indices
     * @return
     */
    boolean exists(String ... indices);

}
