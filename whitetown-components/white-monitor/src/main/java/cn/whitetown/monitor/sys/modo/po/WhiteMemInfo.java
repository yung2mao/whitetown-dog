package cn.whitetown.monitor.sys.modo.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 内存信息 - 单位byte
 * percent - 单位为百分比 * 100 , 例如 98.55% 表示为 9855
 * @author taixian
 * @date 2020/07/31
 **/
@Setter
@Getter
public class WhiteMemInfo implements Serializable {
    private static final long serialVersionUID = 9160942491521617499L;
    private long totalMem;
    private long freeMem;
    private int usedPercent;

    private long timeStamp;
}
