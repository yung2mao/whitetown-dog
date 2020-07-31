package cn.whitetown.monitor.sys.modo.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 当前服务基本信息
 * @author taixian
 * @date 2020/07/31
 **/
@Getter
@Setter
public class ServerInfo {
    private String serverId;
    private String serverName;
    private Date serverStartTime;
    private long workTime;
}
