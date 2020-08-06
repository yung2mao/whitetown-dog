package cn.whitetown.monitor.syslog.dao;

import cn.whitetown.monitor.config.MonConfConstants;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作工具类
 * @author taixian
 * @date 2020/08/06
 **/
public class DefaultDataSourceUtil implements DataSourceUtil{

    private Logger logger = MonConfConstants.logger;

    private DataSource dataSource;

    public static final DefaultDataSourceUtil DS_UTIL = new DefaultDataSourceUtil();

    /**
     * 初始化数据连接池
     */
    private DefaultDataSourceUtil() {
        String driverName = MonConfConstants.DB_DRIVER_NANE;
        String url = MonConfConstants.DB_URL;
        String username = MonConfConstants.DB_USERNAME;
        String password = MonConfConstants.DB_PASSWORD;
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(8);
        druidDataSource.setDriverClassName(driverName);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setUrl(url);
        this.dataSource = druidDataSource;
    }

    @Override
    public Connection getConnect() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnect(Object key) throws SQLException {
        return null;
    }

    @Override
    public <T> void saveOne(String tableName, T entity) {
        if(tableName == null || entity == null) {
            return;
        }
        String sql = this.getBasicSql(tableName,entity);
        Connection connect = null;
        PreparedStatement statement = null;
        try {
            connect = this.getConnect();
            statement = connect.prepareStatement(sql);

            statement.execute();
        }catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            this.close(statement,connect);
        }

    }

    @Override
    public <T> void sageList(String tableName, List<T> entities) {
        if(tableName == null || entities == null || entities.size() == 0) {
            return;
        }
        List<String> sqlList = new LinkedList<>();

    }

    @Override
    public void close(AutoCloseable...autoCloseables){
        for(AutoCloseable autoCloseable:autoCloseables){
            if(autoCloseable != null){
                try {
                    autoCloseable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    autoCloseable = null;
                }
            }
        }
    }

    /**
     * 根据实体类获取构建写入数据库的sql
     * @param tableName
     * @param entity
     * @param <T>
     * @return
     */
    private <T> String getBasicSql(String tableName, T entity) {
        String entityJson = JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue);
        Map entityMap = JSON.parseObject(entityJson, Map.class);
        StringBuilder fieldBuilder = new StringBuilder("");
        StringBuilder valueBuilder = new StringBuilder("");
        for(Object key : entityMap.keySet()) {
            fieldBuilder.append(key).append(",");
            valueBuilder.append("?").append(",");
        }
        String fields = fieldBuilder.toString();
        String values = valueBuilder.toString();
        fields = fields.substring(0,fields.length()-1);
        values = values.substring(0,values.length()-1);
        String sql = "insert into "+tableName+" ("+fields+") values ("+values+");";
        return sql;
    }
}
