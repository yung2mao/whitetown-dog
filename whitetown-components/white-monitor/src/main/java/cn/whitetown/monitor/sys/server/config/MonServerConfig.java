package cn.whitetown.monitor.sys.server.config;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.server.MonitorDao;
import cn.whitetown.monitor.sys.server.wiml.MonScopeSaveManager;
import cn.whitetown.monitor.util.MonitorIDUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端初始化
 * @author taixian
 * @date 2020/08/30
 **/
public class MonServerConfig {

    public static final MonServerConfig CONFIG = new MonServerConfig();

    private MonitorIDUtil monitorIDUtil;

    private List<MonitorDao> monitorDaos;

    /**
     * ID操作对象
     * @return
     */
    public MonitorIDUtil monitorIDUtil() {
        if(monitorIDUtil != null) {
            return monitorIDUtil;
        }
        try {
            Class<?> claz = Class.forName(MonConfConstants.MON_ID_UTIL);
            monitorIDUtil = (MonitorIDUtil)claz.newInstance();
            return monitorIDUtil;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * 获取数据持久化的操作类
     * @return
     */
    public List<MonitorDao> getMonitorDao() {
        if(monitorDaos != null) {
            return monitorDaos;
        }
        List<MonitorDao> monitorDaos = new ArrayList<>();
        String classes = MonConfConstants.SAVE_CLASSES;
        if(classes == null) {
            monitorDaos.add(new MonScopeSaveManager());
            return monitorDaos;
        }
        String[] classArr = classes.split(",");
        try {
            for(String claz : classArr) {
                Class<?> mClass = Class.forName(claz);
                Object obj = mClass.newInstance();
                monitorDaos.add((MonitorDao) obj);
            }
            this.monitorDaos = monitorDaos;
            return monitorDaos;
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
