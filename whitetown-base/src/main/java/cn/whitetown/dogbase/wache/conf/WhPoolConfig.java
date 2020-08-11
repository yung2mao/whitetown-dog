package cn.whitetown.dogbase.wache.conf;

import cn.whitetown.dogbase.wache.BufferElement;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 元素池配置信息
 * @author taixian
 * @date 2020/08/11
 **/
@Setter
@Getter
public class WhPoolConfig<E> {
    private int minIdle;
    private int maxActive;
    private long keepActive;
    private TimeUnit timeUnit;

    public WhPoolConfig() {
        this.minIdle = 4;
        this.maxActive = 20;
        this.keepActive = 60;
        this.timeUnit = TimeUnit.SECONDS;
    }

    public WhPoolConfig(int minIdle, int maxActive, long keepActive, TimeUnit timeUnit) {
        this.minIdle = minIdle;
        this.maxActive = maxActive;
        this.keepActive = keepActive;
        this.timeUnit = timeUnit;
    }
}
