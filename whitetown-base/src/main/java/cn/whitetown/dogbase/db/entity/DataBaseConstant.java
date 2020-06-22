package cn.whitetown.dogbase.db.entity;

/**
 * 与Mybatis数据库操作相关的常量定义
 * @author GrainRain
 * @date 2020/06/21 16:38
 **/
public class DataBaseConstant {
    /**
     * 属性存储名称
     */
    public static final String MEM_FIELD = "dbField";
    /**
     * 方法存储名称
     */
    public static final String MEM_METHOD = "dbMethod";
    /**
     * class反射得到的信息在内存存储的时间
     */
    public static final int CLASS_SAVE_TIME = 180;
}
