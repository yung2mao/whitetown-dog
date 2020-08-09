package cn.whitetown.logbase.pipe.modo;

import com.alibaba.fastjson.JSON;

/**
 * @author taixian
 * @date 2020/08/09
 **/
public class WhClone implements Cloneable {

    @Override
    public Object clone() {
        String text = JSON.toJSONString(this);
        return JSON.parseObject(text,this.getClass());
    }
}
