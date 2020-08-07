package cn.whitetown.monitor.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据源工具类
 * @author taixian
 * @date 2020/08/06
 **/
public interface DataSourceUtil {

    /**
     * 获取连接 - 单库模式使用
     * @throws SQLException
     * @return
     */
    Connection getConnect() throws SQLException;

    /**
     * 获取连接 - 分库模式使用
     * @param key
     * @throws  SQLException
     * @return
     */
    Connection getConnect(Object key) throws SQLException;

    /**
     * 数据存储 - 单条数据
     * @param tableName table名称
     * @param entity
     */
    <T> void saveOne(String tableName ,T entity);

    /**
     * 数据存储 - 批量存储
     * @param tableName 表名称
     * @param entities
     * @param <T>
     */
    <T> void saveList(String tableName , List<T> entities);

    /**
     * 关闭资源
     * @param autoCloseables
     */
    void close(AutoCloseable ... autoCloseables);
}
