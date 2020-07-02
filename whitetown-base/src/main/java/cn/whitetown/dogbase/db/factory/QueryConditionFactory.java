package cn.whitetown.dogbase.db.factory;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author GrainRain
 * @date 2020/06/20 22:17
 **/
public interface QueryConditionFactory {

    /**
     * 根据指定类型创建LambdaQueryWrapper
     * @param claz
     * @param <T>
     * @return
     */
    <T> LambdaQueryWrapper<T> getQueryCondition(Class<T> claz);

    /**
     * 获取
     * @param claz
     * @param <T>
     * @return
     */
    <T> LambdaUpdateWrapper<T> getUpdateCondition(Class<T> claz);

    /**
     * 根据传入对象创建查询条件Wrapper，自动忽略null值
     * @param obj
     * @param claz
     * @param <T>
     * @return
     */
    <T> LambdaQueryWrapper<T> allEqWithNull2IsNull(Object obj,Class<T> claz);

    /**
     * 创建用作分页的Page对象
     * @param page
     * @param size
     * @param claz
     * @param <T>
     * @return
     */
    <T> Page<T> createPage(Integer page,Integer size,Class<T> claz);
}
