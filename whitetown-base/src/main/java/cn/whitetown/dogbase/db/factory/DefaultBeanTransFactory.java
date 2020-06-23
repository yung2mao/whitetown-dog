package cn.whitetown.dogbase.db.factory;

import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 对象转换工厂默认实现
 * @author GrainRain
 * @date 2020/06/22 22:50
 **/
public class DefaultBeanTransFactory implements BeanTransFactory{

    @Autowired
    private WhiteExpireMap expireMap;

    /**
     * beanCopier在内存驻留的时间
     */
    protected static final Integer BEAN_COPIER_KEEP_TIME = 180;

    /**
     * 通过源对象获取目标对象
     * 基于Cglib
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public <T> T trans(Object source, Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        Object key = (source.getClass().getName() + targetClass.getName()).hashCode();
        BeanCopier beanCopier = (BeanCopier)expireMap.get(key);
        if(beanCopier == null) {
            beanCopier = BeanCopier.create(source.getClass(), targetClass, false);
            expireMap.putS(key,beanCopier,BEAN_COPIER_KEEP_TIME);
        }
        expireMap.sExpire(key,BEAN_COPIER_KEEP_TIME);

        T t = targetClass.newInstance();
        beanCopier.copy(source,t, null);
        return t;
    }
}
