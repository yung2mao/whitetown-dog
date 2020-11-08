package cn.whitetown.event.modo;

/**
 * 单个事件信息
 * @Author: taixian
 * @Date: created in 2020/10/26
 */
public class WhiteEvent<T> {
    private String type;
    protected String markKey;
    protected String content;
    private T body;
    private Long createTime;

    public WhiteEvent(String type, String markKey, String content, T body) {
        this.type = type;
        this.markKey = markKey;
        this.content = content;
        this.body = body;
        this.createTime = System.currentTimeMillis();
    }

    public WhiteEvent() {
        this.createTime = System.currentTimeMillis();
    }

    public WhiteEvent(T body) {
        this.body = body;
        this.createTime = System.currentTimeMillis();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarkKey() {
        return markKey;
    }

    public void setMarkKey(String markKey) {
        this.markKey = markKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
