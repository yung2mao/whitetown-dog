package cn.whitetown.monitor.sys.runner.wmil;

import cn.whitetown.monitor.sys.runner.SysMonitorRunner;

import java.util.concurrent.ExecutorService;

/**
 * @author taixian
 * @date 2020/08/03
 **/
public class WhiteScheduleSysMonRun implements SysMonitorRunner {

    private SysMonitorRunner sysMonitorRunner;

    public WhiteScheduleSysMonRun(SysMonitorRunner sysMonitorRunner) {
        this.sysMonitorRunner = sysMonitorRunner;
    }

    @Override
    public void init(ExecutorService executorService) {
        sysMonitorRunner.init(executorService);
    }

    @Override
    public void run() {
        //TODO: schedule
    }

    @Override
    public void open() {
        sysMonitorRunner.open();
    }

    @Override
    public void stop() {
        sysMonitorRunner.stop();
    }

    @Override
    public void destroy() {
        sysMonitorRunner.destroy();
    }
}
