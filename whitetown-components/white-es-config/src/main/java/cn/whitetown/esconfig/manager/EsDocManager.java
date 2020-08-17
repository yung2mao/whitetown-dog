package cn.whitetown.esconfig.manager;

import com.alibaba.fastjson.JSONException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * @throws IOException
     */
    <T> void addDoc(String indexName, String docId, T source) throws IOException;

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
     * 添加文档
     * 索引名称等于source类默认名称
     * @param docId 文档ID
     * @param source 文档信息
     * @param <T> 任意可转为JSON的实体类型
     * @throws IOException
     */
    <T> void addDoc2DefaultIndex(String docId, T source) throws IOException;

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
     * 批量写入
     * @param indexName
     * @param idAndSources
     * @param listener
     * @param <T>
     */
    <T> void addBatch(String indexName, List<Map.Entry<String,T>> idAndSources, ActionListener<BulkResponse> listener);

    /**
     * 批量写入
     * @param idAndSources
     * @param listener
     * @param <T>
     */
    <T> void addBatch(List<Map.Entry<String,T>> idAndSources, ActionListener<BulkResponse> listener);

    /**
     * 按照文档id检索文档
     * @param indexName
     * @param docId
     * @return
     * @throws IOException
     */
    String getDocById(String indexName, String docId) throws IOException;

    /**
     * 根据id获取文档信息 - 直接转换为entity
     * @param indexName
     * @param docId
     * @param claz
     * @param <T>
     * @return
     * @throws JSONException
     * @throws IOException
     */
    <T> T getDocById(String indexName,String docId,Class<T> claz) throws JSONException, IOException;

    /**
     * 更新文档
     * @param indexName
     * @param docId
     * @param newSource
     * @throws IOException
     */
    <T> void updateDoc(String indexName, String docId, T newSource) throws IOException;

    /**
     * 更新文档
     * 异步方式
     * @param indexName
     * @param docId
     * @param newSource
     * @param listener
     * @param <T>
     */
    <T> void updateDoc(String indexName, String docId, T newSource,  ActionListener<UpdateResponse> listener);

    /**
     * 更新文档
     * @param docId
     * @param newSource
     * @throws IOException
     */
    <T> void updateDoc2DefaultIndex(String docId, T newSource) throws IOException;

    /**
     * 更新文档
     * 异步方式
     * @param docId
     * @param newSource
     * @param listener
     * @param <T>
     */
    <T> void updateDoc2DefaultIndex(String docId, T newSource,  ActionListener<UpdateResponse> listener);

}
