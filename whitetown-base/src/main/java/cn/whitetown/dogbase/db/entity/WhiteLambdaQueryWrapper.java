package cn.whitetown.dogbase.db.entity;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * 基于LambadaQueryWrapper构建可以选择Null2IsNull的eq
 * @author GrainRain
 * @date 2020/06/17 20:53
 **/
public class WhiteLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {
    public LambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object val,boolean null2IsNull) {
        if(!null2IsNull) {
            if (val instanceof String) {
                boolean isNull = DataCheckUtil.checkTextNullBool((String) val);
                if (isNull) {
                    return this;
                }
            } else {
                if (val == null) {
                    return this;
                }
            }
        }
        return super.eq(true, column, val);
    }
}
