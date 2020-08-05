package cn.whitetown.monitor.sys.manager;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

import java.io.IOException;

/**
 * 数据写入文件系统管理类
 * @author taixian
 * @date 2020/08/01
 **/
public interface MonitorInfoSaveManager {

    /**
     * 初始化方法
     * @throws IOException
     */
    void init() throws IOException;

    /**
     * 系统监控信息写出保存
     * @param monitorParams
     * @return
     */
    public boolean save(WhiteMonitorParams monitorParams);

    /**
     * 销毁时调用
     */
    void destroy();
}
