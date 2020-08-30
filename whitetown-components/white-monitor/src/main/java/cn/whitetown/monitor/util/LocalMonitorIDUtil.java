package cn.whitetown.monitor.util;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.dao.DataSourceUtil;
import cn.whitetown.monitor.dao.ShardingDataSourceUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author taixian
 * @date 2020/08/30
 **/
public class LocalMonitorIDUtil implements MonitorIDUtil {

    private DataSourceUtil dataSourceUtil = ShardingDataSourceUtil.DS_UTIL;

    private String baseTable = MonConfConstants.BASE_TABLE_NAME;

    private int shardingSize = MonConfConstants.DB_TABLE_SHARDING_SIZE;

    private long shardingScope = MonConfConstants.SHARDING_SCOPE;

    private AtomicLong maxId;

    public LocalMonitorIDUtil() {
        AtomicLong maxId = new AtomicLong();
        for (int i = 0; i < shardingSize; i++) {
            String tableName = baseTable + "_" + i;
            long currentTableMaxId = this.getCurrentTableMaxId(tableName);
            if(currentTableMaxId == 0) {
                maxId.set(shardingScope * i);
                break;
            }
            if(currentTableMaxId < shardingScope * (i+1)) {
                maxId.set(currentTableMaxId);
                break;
            }
            maxId.set(currentTableMaxId);
        }
        this.maxId = maxId;
    }

    @Override
    public Long getId() {
        return maxId.incrementAndGet();
    }


    private long getCurrentTableMaxId(String tableName) {
        Connection connect = null;
        PreparedStatement statement = null;
        try {
            connect = dataSourceUtil.getConnect();
            String sql = "select max(id) as id from " + tableName;
            statement = connect.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");
            return id == null ? 0 : id;
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
