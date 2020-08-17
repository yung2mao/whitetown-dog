package cn.whitetown.esconfig.modo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * es索引信息存储管理
 * @author taixian
 * @date 2020/08/14
 **/
public class EsIndicesMap {
    /**
     * 实体类类名称与索引的关系
     */
    private Map<String,String> classWithIndices;

    public EsIndicesMap(Map<String, String> classWithIndex) {
        this.classWithIndices = classWithIndex;
    }

    public EsIndicesMap() {
        int capacity = 4;
        classWithIndices = new HashMap<>(capacity);
    }

    /**
     * 添加索引信息
     * @param className
     * @param indexName
     * @return
     */
    public String putIndex(String className, String indexName) {
        return classWithIndices.put(className,indexName);
    }

    /**
     * 获取当前类对应es中索引名称
     * @param className
     * @return
     */
    public String getIndexName(String className) {
        return classWithIndices.get(className);
    }

    /**
     * 索引是否存在
     * @param indexName
     * @return
     */
    public boolean containsIndex(String indexName) {
        return classWithIndices.containsValue(indexName);
    }

    /**
     * 移除内存中保存的索引信息
     * @param className
     * @return
     */
    public String removeIndexByClassName(String className) {
        return classWithIndices.remove(className);
    }

    /**
     * 通过indexName从内存移除索引信息
     * @param indexName
     * @return
     */
    public String removeIndexByIndexName(String indexName) {
        for(Map.Entry<String,String> entry : classWithIndices.entrySet()) {
            if(entry.getValue().equals(indexName)) {
                return classWithIndices.remove(entry.getKey());
            }
        }
        return indexName;
    }
}
