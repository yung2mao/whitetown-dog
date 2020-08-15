package cn.whitetown.esconfig.modo;

import com.sun.org.apache.xpath.internal.operations.String;

import java.util.HashMap;
import java.util.Map;

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
     * 移除内存中保存的索引信息
     * @param className
     * @return
     */
    public String removeIndex(String className) {
        return classWithIndices.remove(className);
    }
}
