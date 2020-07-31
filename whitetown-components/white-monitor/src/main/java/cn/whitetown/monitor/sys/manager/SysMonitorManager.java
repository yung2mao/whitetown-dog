package cn.whitetown.monitor.sys.manager;

import cn.whitetown.monitor.sys.modo.po.*;

import java.util.List;

/**
 * 系统信息收集管理
 * @author taixian
 * @date 2020/07/31
 **/
public interface SysMonitorManager {

    /**
     * 获取服务基本信息
     * @return
     */
    ServerInfo getServerInfo();
    /**
     * 获取系统信息
     * @return
     */
    WhiteOsInfo getOsInfo();

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
     * 获取文件读写速率
     * @param newFileInfos
     * @param oldFileInfos
     * @return
     */
    WhiteFileSpeed getFileSpeed(List<WhiteFileInfo> newFileInfos,List<WhiteFileInfo> oldFileInfos);

    /**
     * 获取网络读写速率
     * @param newNetInfo
     * @param oldNetInfo
     * @return
     */
    WhiteNetSpeed getNetSpeed(WhiteNetInfo newNetInfo, WhiteNetInfo oldNetInfo);
}
