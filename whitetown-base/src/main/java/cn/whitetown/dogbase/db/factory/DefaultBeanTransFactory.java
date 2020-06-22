package cn.whitetown.dogbase.db.factory;

import org.springframework.beans.BeanUtils;

/**
 * 对象转换工厂默认实现
 * @author GrainRain
 * @date 2020/06/22 22:50
 **/
public class DefaultBeanTransFactory implements BeanTransFactory{

    /**
     * 通过源对象获取目标对象
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public <T> T trans(Object source, Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        T t = targetClass.newInstance();
        BeanUtils.copyProperties(source,t);
        return t;
    }
}
