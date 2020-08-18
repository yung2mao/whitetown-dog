package cn.whitetown.esconfig.manager.wiml;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.esconfig.manager.EsIndicesManager;
import cn.whitetown.esconfig.modo.EsIndicesMap;
import cn.whitetown.esconfig.utils.EsTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 索引操作
 * @author taixian
 * @date 2020/08/16
 **/
public class DefaultIndicesManager implements EsIndicesManager {

    private EsTools esTools = EsTools.ES_TOOLS;

    private Log log = LogFactory.getLog(DefaultDocManager.class);

    @Autowired
    private EsIndicesMap esIndicesMap;

    @Autowired
    private RestHighLevelClient esClient;

    @Override
    public boolean createIndex(String indexName) {
        try {
            if(indicesExists(indexName)) {
                return true;
            }
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            esClient.indices().create(request,RequestOptions.DEFAULT);
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> boolean createIndex(T entity) {
        String indexName = esTools.getDefaultIndexName(entity);
        if(indexName == null) {
            return false;
        }
        return this.createIndex(indexName,entity);
    }

    @Override
    public <T> boolean createIndex(String indexName, T entity) {
        if(DataCheckUtil.checkTextNullBool(indexName) || entity == null) {
            return false;
        }
        String className = entity.getClass().getName();
        if(esIndicesMap.getIndexName(className) != null) {
            return true;
        }
        Map<String, Map<String, String>> mapping = esTools.createDefaultMapping(entity);
        boolean isCreate = this.createIndexByMapping(indexName, mapping);
        if(isCreate) {
            esIndicesMap.putIndex(className,indexName);
            return true;
        }
        return false;
    }

    @Override
    public boolean createIndexByMapping(String indexName, Map<String, Map<String, String>> fieldsMap) {
        try {
            if(indicesExists(indexName)) {
                return true;
            }
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            XContentBuilder mapping = XContentFactory.jsonBuilder();
            mapping.startObject()
                    .startObject("mappings")
                    .startObject("properties");
            Set<String> fields = fieldsMap.keySet();
            for(String field: fields){
                Map<String, String> fieldType = fieldsMap.get(field);
                mapping.startObject(field);
                for(Map.Entry<String,String> entry :fieldType.entrySet()){
                    mapping.field(entry.getKey(),entry.getValue());
                }
                mapping.endObject();
            }
            mapping.endObject()
                    .endObject()
                    .endObject();
            request.source(mapping);
            esClient.indices().create(request, RequestOptions.DEFAULT);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeIndices(String... indices) {
        DeleteIndexRequest delRequest = new DeleteIndexRequest(indices);
        try {
            esClient.indices().delete(delRequest,RequestOptions.DEFAULT);
            for(String indexName : indices) {
                esIndicesMap.removeIndexByIndexName(indexName);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean indicesExists(String... indices) throws IOException {
        if(indices == null || indices.length == 0) {
            throw new NullPointerException("no indices");
        }
        boolean isExists = true;
        for (String indexName : indices) {
            if(!esIndicesMap.containsIndex(indexName)) {
                isExists = false;
            }
        }
        if(isExists) {
            return true;
        }
        GetIndexRequest indexRequest = new GetIndexRequest(indices);
        return esClient.indices().exists(indexRequest,RequestOptions.DEFAULT);
    }

    @Override
    public <T> boolean entityIndexExists(T entity) {
        if(entity == null) {
            throw new NullPointerException("no index");
        }
        String className = entity.getClass().getName();
        String indexName = esIndicesMap.getIndexName(className);
        if(indexName != null) {
            return true;
        }
        String defaultIndexName = esTools.getDefaultIndexName(entity);
        try {
            boolean exists = this.indicesExists(defaultIndexName);
            if(exists) {
                esIndicesMap.putIndex(className,defaultIndexName);
                return true;
            }
            return false;
        }catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
