package cn.whitetown.monitor.sys.server;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.dao.DataSourceUtil;
import cn.whitetown.monitor.dao.ShardingDataSourceUtil;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控数据存储处理器
 * @author taixian
 * @date 2020/08/08
 **/
public class WhMonScopeSaveHandler {

    private Logger logger = MonConfConstants.logger;

    private static DataSourceUtil dataSourceUtil = ShardingDataSourceUtil.DS_UTIL;

    private static String baseTable = MonConfConstants.BASE_TABLE_NAME;

    private static int shardingSize = MonConfConstants.DB_TABLE_SHARDING_SIZE;

    private static long shardingScope = MonConfConstants.SHARDING_SCOPE;
    /**
     * 当前已存储的最大ID - 需要初始化
     */
    private AtomicLong maxId;

    public WhMonScopeSaveHandler() {
        maxId = new AtomicLong();
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
    }

    public void monSave(WhiteMonitorParams whiteMonitorParams) {
        long newId = maxId.incrementAndGet();
        whiteMonitorParams.setId(newId);
        whiteMonitorParams.setServerId(whiteMonitorParams.getSysBaseInfo().getServerId());
        dataSourceUtil.saveOne(baseTable,whiteMonitorParams);
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
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
