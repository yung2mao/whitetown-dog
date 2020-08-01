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
public class WhiteNetInfo implements Cloneable{
    private String ip;
    private String hostname;
    private String primaryDns;
    private String macAddr;
    private long recBytes;
    private long sentBytes;
    private long errorBytes;

    private long timeStamp;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public WhiteNetInfo cloneObj() {
        Object obj = this.clone();
        return (WhiteNetInfo) obj;
    }
}
