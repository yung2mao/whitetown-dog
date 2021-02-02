package cn.whitetown;

import cn.whitetown.event.factory.AbstractEventFactory;
import cn.whitetown.event.modo.WhiteEvent;
import org.junit.Test;

/**
 * @Author: taixian
 * @Date: created in 2020/11/14
 */
public class EventTest {

    @Test
    public void createEvent() {
        AbstractEventFactory eventFactory = new AbstractEventFactory() {
        };
        WhiteEvent<String> event = eventFactory.createEvent("hello");
        System.out.println(event);
    }
}
