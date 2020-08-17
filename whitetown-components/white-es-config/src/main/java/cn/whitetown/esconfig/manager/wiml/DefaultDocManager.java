package cn.whitetown.esconfig.manager.wiml;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.esconfig.manager.EsDocManager;
import cn.whitetown.esconfig.modo.EsIndicesMap;
import cn.whitetown.esconfig.utils.EsTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文档操作
 * @author taixian
 * @date 2020/08/16
 **/
public class DefaultDocManager implements EsDocManager {

    @Autowired
    private RestHighLevelClient esClient;

    @Autowired
    private SnowIDCreateUtil idCreateUtil;

    @Autowired
    private EsIndicesMap indicesMap;

    private EsTools esTools = EsTools.ES_TOOLS;

    @Override
    public <T> void addDoc(String indexName, String docId, T source) throws IOException {
        if(source == null) {
            return;
        }
        IndexRequest request = this.createPutRequest(indexName, docId, source);
        esClient.index(request, RequestOptions.DEFAULT);
    }

    @Override
    public <T> void addDoc(String indexName, String docId, T source, ActionListener<IndexResponse> listener) {
        if(source == null) {
            return;
        }
        IndexRequest request = this.createPutRequest(indexName, docId, source);
        if(listener == null) {
            listener = esTools.defaultIndexListener();
        }
        esClient.indexAsync(request, RequestOptions.DEFAULT, listener);
    }

    @Override
    public <T> void addDoc2DefaultIndex(String docId, T source) throws IOException {
        this.addDoc(null,docId,source);
    }

    @Override
    public <T> void addDoc2DefaultIndex(String docId, T source, ActionListener<IndexResponse> listener) {
        this.addDoc(null,docId,source,listener);
    }

    @Override
    public <T> void addBatch(String indexName, List<Map.Entry<String, T>> idAndSources, ActionListener<BulkResponse> listener) {
        if(indexName == null || idAndSources.size() == 0) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for(Map.Entry<String,T> entry : idAndSources) {
            IndexRequest request = createPutRequest(indexName, entry.getKey(), entry.getValue());
            bulkRequest.add(request);
        }
        if(listener == null) {
            listener = esTools.defaultBulkListener();
        }
        esClient.bulkAsync(bulkRequest,RequestOptions.DEFAULT,listener);
    }

    @Override
    public <T> void addBatch(List<Map.Entry<String,T>> idAndSources, ActionListener<BulkResponse> listener) {
        String indexName = null;
        T value = idAndSources.get(0).getValue();
        if(value == null) {
            return;
        }
        indexName = indicesMap.getIndexName(value.getClass().getName());
        if(indexName == null) {
            indexName = esTools.getDefaultIndexName(value);
        }
        this.addBatch(indexName,idAndSources,listener);
    }

    @Override
    public String getDocById(String indexName, String docId) throws IOException {
        if(indexName == null || docId == null) {
            return null;
        }
        GetRequest request = new GetRequest(indexName);
        request.id(docId);
        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        return sourceAsString;
    }

    @Override
    public <T> T getDocById(String indexName, String docId, Class<T> claz) throws JSONException, IOException {
        String doc = this.getDocById(indexName, docId);
        if(doc == null) {
            return null;
        }
        try {
            return JSON.parseObject(doc,claz);
        }catch (Exception e) {
            throw new JSONException(e.getMessage());
        }
    }

    @Override
    public <T> void updateDoc(String indexName, String docId, T newSource) throws IOException {
        if(newSource == null) {
            return;
        }
        UpdateRequest updateRequest = this.createUpdateRequest(indexName, docId, newSource);
        esClient.update(updateRequest,RequestOptions.DEFAULT);
    }

    @Override
    public <T> void updateDoc(String indexName, String docId, T newSource, ActionListener<UpdateResponse> listener) {
        if(newSource == null) {
            return;
        }
        if(listener == null) {
            listener = esTools.defaultUpdateListener();
        }
        UpdateRequest updateRequest = this.createUpdateRequest(indexName, docId, newSource);
        esClient.updateAsync(updateRequest,RequestOptions.DEFAULT,listener);
    }

    @Override
    public <T> void updateDoc2DefaultIndex(String docId, T newSource) throws IOException {
        this.updateDoc(null,docId,newSource);
    }

    @Override
    public <T> void updateDoc2DefaultIndex(String docId, T newSource, ActionListener<UpdateResponse> listener) {
        this.updateDoc(null,docId,newSource,listener);
    }

    /*-----------------------------private method-----------------------------------------*/

    /**
     * 构建添加文档操作的request
     * @param indexName
     * @param docId
     * @param source
     * @param <T>
     * @return
     */
    private <T> IndexRequest createPutRequest(String indexName,String docId,T source) {
        if(source == null) {
            throw new NullPointerException("no source");
        }
        if (indexName == null) {
            indexName = esTools.getDefaultIndexName(source);
        }
        IndexRequest request = new IndexRequest(indexName);
        if(DataCheckUtil.checkTextNullBool(docId)) {
            String defaultIdFieldName = "id";
            Object id = WhiteToolUtil.getFieldValue(defaultIdFieldName,source);
            docId = id == null ? idCreateUtil.getSnowId()+"" : String.valueOf(id);
        }
        request.id(docId);
        request.source(JSON.toJSONString(source), XContentType.JSON);
        return request;
    }

    /**
     * 获取updateRequest
     * @param indexName
     * @param docId
     * @param newSource
     * @param <T>
     * @return
     */
    private <T> UpdateRequest createUpdateRequest(String indexName, String docId, T newSource) {
        if(newSource == null) {
            throw new NullPointerException("no new source");
        }
        if(indexName == null) {
            indexName = esTools.getDefaultIndexName(newSource);
        }
        if(DataCheckUtil.checkTextNullBool(docId)) {
            String defaultIdFieldName = "id";
            Object id = WhiteToolUtil.getFieldValue(defaultIdFieldName,newSource);
            docId = id == null ? idCreateUtil.getSnowId()+"" : String.valueOf(id);
        }
        UpdateRequest request = new UpdateRequest();
        request.index(indexName);
        request.id(docId);
        request.doc(JSON.toJSONString(newSource),XContentType.JSON);
        return request;
    }
}
