package cn.whitetown.mshow.modo.sys;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务基本信息
 * @author taixian
 * @date 2020/08/18
 **/
@Getter
@Setter
public class ServiceInfo {
    private String serverId;
    private String aliasName;
    private int serverStatus;
    private String serverIp;
}
