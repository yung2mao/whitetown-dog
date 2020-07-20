package cn.whitetown.dogbase.db.entity;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import sun.security.jca.GetInstance;

/**
 * 基于LambadaQueryWrapper构建可以选择Null2IsNull的自定义QueryWrapper
 * @author GrainRain
 * @date 2020/06/17 20:53
 **/
public class WhiteLambdaQueryWrapper<T>{

    private LambdaQueryWrapper<T> queryWrapper;

    private WhiteLambdaQueryWrapper(LambdaQueryWrapper queryWrapper){
        this.queryWrapper = queryWrapper;
    }

    public static <T> WhiteLambdaQueryWrapper getInstance(LambdaQueryWrapper<T> queryWrapper) {
        return new WhiteLambdaQueryWrapper(queryWrapper);
    }

    public WhiteLambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object val,boolean null2IsNull) {
        if(this.checkReturnThis(val,null2IsNull)){
            return this;
        }
        queryWrapper.eq(column,val);
        return this;
    }

    public WhiteLambdaQueryWrapper<T> gt(SFunction<T, ?> column, Object val,boolean null2IsNull) {
        if(this.checkReturnThis(val,null2IsNull)){
            return this;
        }
        queryWrapper.gt(column,val);
        return this;
    }

    public WhiteLambdaQueryWrapper<T> ge(SFunction<T, ?> column, Object val,boolean null2IsNull) {
        if(this.checkReturnThis(val,null2IsNull)){
            return this;
        }
        queryWrapper.ge(column,val);
        return this;
    }

    public WhiteLambdaQueryWrapper<T> lt(SFunction<T, ?> column, Object val,boolean null2IsNull) {
        if(this.checkReturnThis(val,null2IsNull)){
            return this;
        }
        queryWrapper.lt(column,val);
        return this;
    }

    public WhiteLambdaQueryWrapper<T> le(SFunction<T, ?> column, Object val,boolean null2IsNull) {
        if(this.checkReturnThis(val,null2IsNull)){
            return this;
        }
        queryWrapper.le(column,val);
        return this;
    }

    public WhiteLambdaQueryWrapper<T> between(SFunction<T, ?> column, Object val1, Object val2, boolean null2IsNull) {
        if(!this.checkReturnThis(val1,null2IsNull)) {
            queryWrapper.gt(column,val1);
        }
        if(!this.checkReturnThis(val2,null2IsNull)) {
            queryWrapper.le(column,val2);
        }
        return this;
    }

    public WhiteLambdaQueryWrapper<T> like(SFunction<T, ?> column, Object val,boolean null2IsNull) {
        if(this.checkReturnThis(val,null2IsNull)){
            return this;
        }
        queryWrapper.like(column,val);
        return this;
    }

    public LambdaQueryWrapper<T> getLambdaQueryWrapper(){
        return queryWrapper;
    }

    /**
     * 检查是否返回本身
     * @param val
     * @param null2IsNull
     * @return
     */
    protected boolean checkReturnThis(Object val,boolean null2IsNull) {
        if(!null2IsNull) {
            if(val instanceof String) {
                if(DataCheckUtil.checkTextNullBool((String)val)) {
                    return true;
                }
            }else {
                if(val == null) {
                    return true;
                }
            }
        }
        return false;
    }
}
