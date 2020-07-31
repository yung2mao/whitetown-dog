package cn.whitetown.monitor.sys.modo.po;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 网络传输速度
 * 指定时间范围内的平均速度
 * @author taixian
 * @date 2020/07/31
 **/
@Getter
@Setter
public class WhiteNetSpeed {
    private long receiveSpeed;
    private long sendSpeed;
    private long errorSpeed;

    private long timeStamp;

    public WhiteNetSpeed() {
    }

    public WhiteNetSpeed(long receiveSpeed, long sendSpeed, long errorSpeed) {
        this.receiveSpeed = receiveSpeed;
        this.sendSpeed = sendSpeed;
        this.errorSpeed = errorSpeed;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
