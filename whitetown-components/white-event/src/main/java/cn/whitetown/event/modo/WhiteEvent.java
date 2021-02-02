package cn.whitetown.event.modo;

import cn.whitetown.event.enums.EventTypeEnum;
import com.alibaba.fastjson.JSON;

/**
 * 单个事件信息
 * @Author: taixian
 * @Date: created in 2020/10/26
 */
public class WhiteEvent<T> {
    private EventTypeEnum type;
    protected String markKey;
    protected String content;
    private T body;
    private Long publishTime;

    public WhiteEvent(EventTypeEnum type, String markKey, String content, T body) {
        this.type = type;
        this.markKey = markKey;
        this.content = content;
        this.body = body;
        this.publishTime = System.currentTimeMillis();
    }

    public WhiteEvent() {
        this.publishTime = System.currentTimeMillis();
    }

    public WhiteEvent(T body) {
        this.body = body;
        this.publishTime = System.currentTimeMillis();
    }

    public EventTypeEnum getType() {
        return type;
    }

    public void setType(EventTypeEnum type) {
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

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
