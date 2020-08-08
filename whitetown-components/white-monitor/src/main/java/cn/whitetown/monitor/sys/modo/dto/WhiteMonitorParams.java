package cn.whitetown.monitor.sys.modo.dto;

import cn.whitetown.monitor.dao.WhiteTableKey;
import cn.whitetown.monitor.sys.modo.po.*;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 系统信息汇总类
 * @author taixian
 * @date 2020/07/31
 **/
@Getter
@Setter
public class WhiteMonitorParams {
    @WhiteTableKey
    private Long id;
    private String serverId;
    private WhiteSysBaseInfo sysBaseInfo;
    private WhiteCpuInfo whiteCpuInfo;
    private WhiteMemInfo memInfo;
    private WhiteJvmInfo jvmInfo;
    private List<WhiteFileInfo> fileInfos;
    private WhiteNetInfo netInfo;
    private WhiteNetSpeed netSpeed;
    private String timeStamp;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
