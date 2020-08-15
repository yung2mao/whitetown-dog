package cn.whitetown.esconfig.manager;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;

/**
 * 文档操作
 * @author taixian
 * @date 2020/08/14
 **/
public interface EsDocManager {

    /**
     * 添加文档
     * @param indexName 索引名称
     * @param docId 文档ID
     * @param source 存储的文档信息
     * @param <T> 任意可转换为JSON的实体类型
     */
    <T> void addDoc(String indexName, String docId, T source);

    /**
     * 添加文档
     * 索引名称等于source类默认名称
     * @param docId 文档ID
     * @param source 文档信息
     * @param <T> 任意可转为JSON的实体类型
     */
    <T> void addDoc2DefaultIndex(String docId, T source);

    /**
     * 异步添加文档
     * 索引名称等于source类默认名称
     * @param docId
     * @param source
     * @param listener
     * @param <T>
     */
    <T> void addDoc2DefaultIndex(String docId, T source,  ActionListener<IndexResponse> listener);

    /**
     * 异步方式添加文档
     * @param indexName
     * @param docId
     * @param source 文档信息
     * @param listener
     * @param <T>
     */
    <T> void addDoc(String indexName, String docId, T source, ActionListener<IndexResponse> listener);

    /**
     * 按照文档id检索文档
     * @param indexName
     * @param docId
     * @return
     */
    String getDocById(String indexName, String docId);

    /**
     * 更新文档
     * @param indexName
     * @param docId
     * @param newSource
     */
    <T> void updateDoc(String indexName, String docId, T newSource);

    /**
     * 更新文档
     * 异步方式
     * @param indexName
     * @param docId
     * @param newSource
     * @param listener
     * @param <T>
     */
    <T> void updateDoc(String indexName, String docId, T newSource,  ActionListener<IndexResponse> listener);

}
