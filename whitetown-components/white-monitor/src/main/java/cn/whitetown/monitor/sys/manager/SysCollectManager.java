package cn.whitetown.monitor.sys.manager;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.monitor.sys.modo.po.*;

import java.util.List;

/**
 * 系统信息收集管理
 * @author taixian
 * @date 2020/07/31
 **/
public interface SysCollectManager {
    /**
     * 获取系统信息
     * @return
     */
    WhiteSysBaseInfo getSysBaseInfo();

    /**
     * 获取CPU信息
     * @return
     */
    WhiteCpuInfo getCpuInfo();

    /**
     * 获取内存信息
     * @return
     */
    WhiteMemInfo getMemoryInfo();

    /**
     * 获取Jvm信息
     * @return
     */
    WhiteJvmInfo getJvmInfo();

    /**
     * 获取文件系统信息
     * @return
     */
    List<WhiteFileInfo> getFileInfo();

    /**
     * 获取网络信息
     * @return
     */
    WhiteNetInfo getNetInfo();

    /**
     * 获取网络读写速率
     * @return
     */
    WhiteNetSpeed getNetSpeed();

    /**
     * 监控信息组装到一起
     * @return
     */
    WhiteMonitorParams createMonitorParams();
}
