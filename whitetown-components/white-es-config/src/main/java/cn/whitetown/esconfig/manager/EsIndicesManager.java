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
     * @throws IOException
     */
    void createIndex(String indexName) throws IOException;

    /**
     * 根据实体类创建索引
     * 默认索引名称等于类名称小写形式
     * @param entity
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> void createIndex(T entity) throws IOException;

    /**
     * 根据索引名称, 任意entity创建索引
     * 根据entity中字段类型和初始值创建默认类型的mapping
     * @param indexName 索引名称
     * @param entity 索引mapping对应的实体类
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> void createIndex(String indexName,T entity) throws IOException;

    /**
     * 根据索引名称,创建具有静态mapping的索引
     * @param indexName
     * @param fieldsMap
     * @return
     * @throws IOException
     */
    void createIndexByMapping(String indexName, Map<String, Map<String,String>> fieldsMap) throws IOException;

    /**
     * 删除索引
     * @param indices
     * @return
     * @throws IOException
     */
    void removeIndices(String ... indices) throws IOException;

    /**
     * 索引信息是否存在
     * @param indices
     * @return
     * @throws IOException
     */
    boolean indicesExists(String ... indices) throws IOException;

    /**
     * 判断entity对应索引信息是否存在
     * @param entity
     * @param <T>
     * @return
     */
    <T> boolean entityIndexExists(T entity);

}
