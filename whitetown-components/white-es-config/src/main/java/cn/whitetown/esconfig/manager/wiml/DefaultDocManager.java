package cn.whitetown.esconfig.manager.wiml;

import cn.whitetown.dogbase.common.util.BaseIDCreateUtil;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.esconfig.manager.EsDocManager;
import cn.whitetown.esconfig.utils.EsTools;
import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 文档操作
 * @author taixian
 * @date 2020/08/16
 **/
public class DefaultDocManager implements EsDocManager {

    private Log log = LogFactory.getLog(DefaultDocManager.class);

    @Autowired
    private RestHighLevelClient esClient;

    @Autowired
    private SnowIDCreateUtil idCreateUtil;

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
            listener = defaultListener();
        }
        esClient.indexAsync(request, RequestOptions.DEFAULT, listener);
    }

    @Override
    public <T> void addDoc2DefaultIndex(String docId, T source) throws IOException {
        if(source == null) {
            return;
        }
        this.addDoc(null,docId,source);
    }

    @Override
    public <T> void addDoc2DefaultIndex(String docId, T source, ActionListener<IndexResponse> listener) {
        if(source == null) {
            return;
        }
        this.addDoc(null,docId,source,listener);
    }


    @Override
    public String getDocById(String indexName, String docId) {
        return null;
    }

    @Override
    public <T> void updateDoc(String indexName, String docId, T newSource) {

    }

    @Override
    public <T> void updateDoc(String indexName, String docId, T newSource, ActionListener<IndexResponse> listener) {

    }

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
     * 默认异步处理默认监听器
     * @return
     */
    private ActionListener<IndexResponse> defaultListener() {
        return new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse response) {
                log.info("response success");
            }

            @Override
            public void onFailure(Exception e) {
                log.warn("failed >> "+e.getMessage());
            }
        };
    }
}
