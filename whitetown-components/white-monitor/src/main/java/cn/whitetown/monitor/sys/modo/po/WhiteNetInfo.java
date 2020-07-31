package cn.whitetown.monitor.sys.modo.po;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 网络基本信息
 * @author taixian
 * @date 2020/07/31
 **/
@Getter
@Setter
public class WhiteNetInfo {
    private String ip;
    private String hostname;
    private String primaryDNS;
    private String MACAddr;
    private long readBytes;
    private long writeBytes;
    private long errorBytes;

    private long timeStamp;

    public WhiteNetInfo() {
    }

    public WhiteNetInfo(String ip, String hostname, String primaryDNS, String MACAddr) {
        this.ip = ip;
        this.hostname = hostname;
        this.primaryDNS = primaryDNS;
        this.MACAddr = MACAddr;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
