package cn.whitetown.esconfig.modo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author taixian
 * @date 2020/08/17
 **/
@Getter
@Setter
public class EsRange {
    private String key;
    private Double from;
    private Double to;

    public EsRange(String key, Double from, Double to) {
        this.key = key;
        this.from = from;
        this.to = to;
    }
}
