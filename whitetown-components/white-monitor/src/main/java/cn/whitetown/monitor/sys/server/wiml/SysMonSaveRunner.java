package cn.whitetown.monitor.sys.server.wiml;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.monitor.sys.server.MonitorDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 接收数据存储
 * @author taixian
 * @date 2020/08/04
 **/
public class SysMonSaveRunner implements Runnable {

    private static List<MonitorDao> saveDaos;

    private WhiteMonitorParams whiteMonitorParams;

    public SysMonSaveRunner() {
        saveDaos = new ArrayList<>();
    }

    public SysMonSaveRunner(WhiteMonitorParams whiteMonitorParams) {
        this.whiteMonitorParams = whiteMonitorParams;
    }

    public static void addSaveDao(MonitorDao saveDao) {
        saveDaos.add(saveDao);
    }

    public static void setSaveDaos(List<MonitorDao> saveDaos) {
        SysMonSaveRunner.saveDaos = saveDaos;
    }

    @Override
    public void run() {
        if(saveDaos.size() == 0) {
            saveDaos.add(new MonScopeSaveManager());
        }
        saveDaos.forEach(dao->dao.save(whiteMonitorParams));
    }
}

