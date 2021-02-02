package cn.whitetown.event.factory;

import cn.whitetown.event.enums.EventTypeEnum;
import cn.whitetown.event.modo.WhiteEvent;
import cn.whitetown.event.util.WhiteToolUtil;

/**
 * @Author: taixian
 * @Date: created in 2020/11/14
 */
public abstract class AbstractEventFactory implements EventFactory{

    protected String defaultContentPrefix = "event-";

    @Override
    public <T> WhiteEvent<T> createEvent(T body) {
        assert body != null;
        int defaultMarkKeyLen = 16;
        String markKey = WhiteToolUtil.createRandomString(defaultMarkKeyLen);
        return this.createEvent(markKey, body);
    }

    @Override
    public <T> WhiteEvent<T> createEvent(String markKey, T body) {
        assert body != null;
        assert markKey != null;
        EventTypeEnum defaultType = EventTypeEnum.DEFAULT;
        String content = createContent(defaultType);
        return new WhiteEvent<>(defaultType, markKey, content, body);
    }

    protected String createContent(Object type) {
        return defaultContentPrefix + type;
    }
}
