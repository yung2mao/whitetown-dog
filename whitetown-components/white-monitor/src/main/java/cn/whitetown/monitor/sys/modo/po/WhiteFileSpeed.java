package cn.whitetown.monitor.sys.modo.po;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件系统读写速度
 * 计算指定时间范围内每秒平均速度
 * @author taixian
 * @date 2020/07/31
 **/
@Getter
@Setter
public class WhiteFileSpeed {

    private long readSpeed;
    private long writeSpeed;

    private long timeStamp;

    public WhiteFileSpeed() {
    }

    public WhiteFileSpeed(long readSpeed, long writeSpeed) {
        this.readSpeed = readSpeed;
        this.writeSpeed = writeSpeed;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
