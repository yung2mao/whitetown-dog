package cn.whitetown.monitor.dao;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.monitor.config.MonConfConstants;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.jna.platform.win32.Ddeml;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库操作工具类
 * @author taixian
 * @date 2020/08/06
 **/
public class ShardingDataSourceUtil implements DataSourceUtil{

    private Logger logger = LogConstants.DB_LOGGER;

    private ShadingUtil shadingUtil = DefaultShardingUtil.SHARDING_UTIL;

    private DataSource dataSource;

    public static final ShardingDataSourceUtil DS_UTIL = new ShardingDataSourceUtil();

    /**
     * 初始化数据连接池
     */
    private ShardingDataSourceUtil() {
        String driverName = MonConfConstants.DB_DRIVER_NANE;
        String url = MonConfConstants.DB_URL;
        String username = MonConfConstants.DB_USERNAME;
        String password = MonConfConstants.DB_PASSWORD;
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setMinIdle(MonConfConstants.DB_POOL_MIN_IDLE);
        druidDataSource.setMaxActive(MonConfConstants.DB_POOL_MAX_ACTIVE);
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
        if(tableName == null || "".equals(tableName) || entity == null) {
            return;
        }
        Integer index = null;
        try {
            index = shadingUtil.scopeSharding(entity);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
        tableName = tableName + "_" + index;
        Map entityMap = this.entityToMap(entity);
        String sql = this.getBasicSql(tableName,entityMap);
        Connection connect = null;
        PreparedStatement statement = null;
        try {
            connect = this.getConnect();
            statement = connect.prepareStatement(sql);
            this.setValues(statement,entityMap);
            statement.execute();
        }catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            this.close(statement,connect);
        }
    }

    @Override
    public <T> void saveList(String tableName, List<T> entities) {
        if(tableName == null || "".equals(tableName) || entities == null || entities.size() == 0) {
            return;
        }
        Integer index = null;
        try {
            index = shadingUtil.scopeSharding(entities.get(0));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
        String realTableName = tableName + "_" + index;
        Connection connect = null;
        PreparedStatement statement = null;
        String sql = this.getBasicSql(realTableName,this.entityToMap(entities.get(0)));
        try {
            connect = this.getConnect();
            statement = connect.prepareStatement(sql);
            for (int i = 0; i < entities.size(); i++) {
                T t = entities.get(i);
                int index2 = shadingUtil.scopeSharding(t);
                if(index != index2 || i > 1000) {
                    statement.executeBatch();
                    realTableName = tableName + "_" + index2;
                    String basicSql = this.getBasicSql(realTableName, this.entityToMap(t));
                    statement = connect.prepareStatement(basicSql);
                    index = index2;
                }
                statement = this.setValues(statement, this.entityToMap(t));
                statement.addBatch();
            }
            statement.executeBatch();
        }catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            this.close(statement,connect);
        }
    }

    @Override
    public void close(AutoCloseable...autoCloseables){
        for(AutoCloseable autoCloseable:autoCloseables){
            if(autoCloseable != null){
                try {
                    autoCloseable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private <T> Map entityToMap(T entity) {
        String entityJson = JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue);
        Map entityMap = JSON.parseObject(entityJson, Map.class);
        return entityMap;
    }

    /**
     * 根据实体类获取构建写入数据库的sql
     * @param tableName
     * @param entityMap
     * @return
     */
    private String getBasicSql(String tableName, Map entityMap) {
        StringBuilder fieldBuilder = new StringBuilder("");
        StringBuilder valueBuilder = new StringBuilder("");
        for(Object key : entityMap.keySet()) {
            fieldBuilder.append(this.fieldToCol(key.toString())).append(",");
            valueBuilder.append("?").append(",");
        }
        String fields = fieldBuilder.toString();
        String values = valueBuilder.toString();
        fields = fields.substring(0,fields.length()-1);
        values = values.substring(0,values.length()-1);
        String sql = "insert into "+tableName+" ("+fields+") values ("+values+");";
        return sql;
    }

    private String fieldToCol(String key) {
        if(key == null) {
            throw new NullPointerException();
        }
        StringBuilder builder = new StringBuilder();
        char[] fieldChars = key.toCharArray();
        for (int i = 0; i < fieldChars.length; i++) {
            if(fieldChars[i] >= 'A' && fieldChars[i] <= 'Z'){
                builder.append("_").append(String.valueOf(fieldChars[i]).toLowerCase());
            }else {
                builder.append(fieldChars[i]);
            }
        }
        return builder.toString();
    }

    /**
     * sql赋值
     * @param statement
     * @param entityMap
     * @return
     */
    private PreparedStatement setValues(PreparedStatement statement, Map entityMap) throws SQLException {
        Set set = entityMap.keySet();
        int index = 1;
        for(Object key: set) {
            Object value = entityMap.get(key);
            statement.setObject(index,value);
            index ++;
        }
        return statement;
    }
}
