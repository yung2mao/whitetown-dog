package cn.whitetown.esconfig.config;

/**
 * @author taixian
 * @date 2020/08/16
 **/
public enum  EsConfigEnum {
    /*\******es字段类型******\*/
    /**
     * type/keyword
     */
    KEYWORD("type","keyword"),
    /**
     * type/text
     */
    TEXT("type","text"),
    /**
     * type/byte
     */
    ES_BYTE("type","byte"),
    /**
     * type/integer
     */
    ES_INT("type","integer"),
    /**
     * type/long
     */
    ES_LONG("type","long"),
    /**
     * type/double
     */
    ES_DOUBLE("type","double"),
    /**
     * type/boolean
     */
    ES_BOOLEAN("type","boolean"),
    /**
     * type/date
     */
    ES_DATE("type","date"),
    /**
     * 可选日期格式
     */
    ES_DATE_FORMAT("format","yyyy-MM-dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd||epoch_millis"),
    /**
     * type/ip
     */
    ES_IP("type","ip"),
    /**
     * type/object
     */
    ES_OBJECT("type","object"),
    /**
     * type/array
     */
    ES_ARRAY("type","array"),
    /**
     * 指定中文IK分词器
     */
    ES_IK("analyzer","ik_smart"),
    /**
     * 指定标准分词器
     */
    ES_STANDARD("analyzer","standard");

    private String key;
    private String value;

    EsConfigEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
