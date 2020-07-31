package cn.whitetown.monitor.sys.modo.dto;

import cn.whitetown.monitor.sys.modo.po.*;
import lombok.Getter;
import lombok.Setter;
/**
 * 系统信息汇总类
 * @author taixian
 * @date 2020/07/31
 **/
@Getter
@Setter
public class WhiteMonitorParams {
    private ServerInfo serverInfo;
    private WhiteOsInfo osInfo;
    private WhiteCpuInfo whiteCpuInfo;
    private WhiteMemInfo memInfo;
    private WhiteJvmInfo jvmInfo;
    private WhiteFileInfo fileInfo;
    private WhiteFileSpeed fileSpeed;
    private WhiteNetInfo netInfo;
    private WhiteNetSpeed netSpeed;
    private long timeStamp;
}
