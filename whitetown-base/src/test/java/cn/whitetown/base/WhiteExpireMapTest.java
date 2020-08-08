package cn.whitetown.base;

import cn.whitetown.dogbase.wache.WhiteExpireMap;
import cn.whitetown.dogbase.wache.wmil.MultiWhiteExpireMap;
import cn.whitetown.dogbase.wache.wmil.SingleWhiteExpireMap;
import cn.whitetown.monitor.sys.client.wmil.ScheduleSysMonRun;
import org.junit.Test;

/**
 * @author taixian
 * @date 2020/08/08
 **/
public class WhiteExpireMapTest {

    @Test
    public void putTest() {
        WhiteExpireMap<Long, Object> expireMap = new MultiWhiteExpireMap<>();

        long i = 0;
        long start = System.currentTimeMillis();
        while (true) {
            try {
                expireMap.put(i++,new byte[1024],1024);
            }catch (Exception e) {
                long end = System.currentTimeMillis();
                System.out.println(end - start);
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
