package cn.whitetown.dogbase.db.factory;

/**
 * 对象转换工厂
 * @author GrainRain
 * @date 2020/06/22 22:49
 **/
public interface BeanTransFactory {

    /**
     * 通过源对象获取目标对象
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    <T> T trans(Object source,Class<T> targetClass) throws IllegalAccessException, InstantiationException;
}
