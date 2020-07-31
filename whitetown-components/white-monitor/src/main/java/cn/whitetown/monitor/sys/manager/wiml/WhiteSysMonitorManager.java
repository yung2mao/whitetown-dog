package cn.whitetown.monitor.sys.manager.wiml;

import cn.whitetown.monitor.sys.manager.SysMonitorManager;
import cn.whitetown.monitor.sys.modo.po.*;
import org.junit.Test;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;
import java.util.Properties;

/**
 * 监控管理实现
 * @author taixian
 * @date 2020/07/31
 **/
public class WhiteSysMonitorManager implements SysMonitorManager {

    private SystemInfo systemInfo;
    private HardwareAbstractionLayer hardware;
    private Properties sysProperties;

    public WhiteSysMonitorManager(SystemInfo systemInfo, HardwareAbstractionLayer hardware) {
        this.systemInfo = systemInfo;
        this.hardware = hardware;
        sysProperties = System.getProperties();
    }

    public WhiteSysMonitorManager() {
        this.systemInfo = new SystemInfo();
        this.hardware = systemInfo.getHardware();
    }

    @Override
    public ServerInfo getServerInfo() {
        return new ServerInfo();
    }

    @Override
    public WhiteOsInfo getOsInfo() {
        return null;
    }

    @Override
    public WhiteCpuInfo getCpuInfo() {
        WhiteCpuInfo whiteCpuInfo = new WhiteCpuInfo();
        CentralProcessor cpuInfo = hardware.getProcessor();
        whiteCpuInfo.setName(cpuInfo.getName());
        whiteCpuInfo.setVendor(cpuInfo.getVendor());
        whiteCpuInfo.setModel(cpuInfo.getModel());
        whiteCpuInfo.setTotalCores(cpuInfo.getLogicalProcessorCount());
        return whiteCpuInfo;
    }

    @Override
    public WhiteMemInfo getMemoryInfo() {
        return null;
    }

    @Override
    public WhiteJvmInfo getJvmInfo() {
        return null;
    }

    @Override
    public List<WhiteFileInfo> getFileInfo() {
        return null;
    }

    @Override
    public WhiteNetInfo getNetInfo() {
        return null;
    }

    @Override
    public WhiteFileSpeed getFileSpeed(List<WhiteFileInfo> newFileInfos, List<WhiteFileInfo> oldFileInfos) {
        return null;
    }

    @Override
    public WhiteNetSpeed getNetSpeed(WhiteNetInfo newNetInfo, WhiteNetInfo oldNetInfo) {
        return null;
    }
}
