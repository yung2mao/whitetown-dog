package cn.whitetown.monitor.sys.server.wiml;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.dao.DataSourceUtil;
import cn.whitetown.monitor.dao.ShardingDataSourceUtil;
import cn.whitetown.monitor.sys.modo.dto.WhMonSaveParam;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import com.alibaba.fastjson.JSON;
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
public class MonScopeSaveManager {

    private Logger logger = LogConstants.sysLogger;

    private static DataSourceUtil dataSourceUtil = ShardingDataSourceUtil.DS_UTIL;

    private static String baseTable = MonConfConstants.BASE_TABLE_NAME;

    private static int shardingSize = MonConfConstants.DB_TABLE_SHARDING_SIZE;

    private static long shardingScope = MonConfConstants.SHARDING_SCOPE;
    /**
     * 当前已存储的最大ID
     */
    private AtomicLong maxId;

    public MonScopeSaveManager() {
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
        WhMonSaveParam whMonSaveParam = new WhMonSaveParam();
        whMonSaveParam.setId(newId);
        whMonSaveParam.setServerId(whiteMonitorParams.getSysBaseInfo().getServerId());
        whMonSaveParam.setSysBaseInfo(whiteMonitorParams.getSysBaseInfo().toString());
        whMonSaveParam.setWhiteCpuInfo(whiteMonitorParams.getWhiteCpuInfo().toString());
        whMonSaveParam.setMemInfo(whiteMonitorParams.getMemInfo().toString());
        whMonSaveParam.setJvmInfo(whiteMonitorParams.getJvmInfo().toString());
        whMonSaveParam.setFileInfos(JSON.toJSONString(whiteMonitorParams.getFileInfos()));
        whMonSaveParam.setNetInfo(whiteMonitorParams.getNetInfo().toString());
        whMonSaveParam.setNetSpeed(whiteMonitorParams.getNetSpeed().toString());
        whMonSaveParam.setTimeStamp(whiteMonitorParams.getTimeStamp());
        dataSourceUtil.saveOne(baseTable,whMonSaveParam);
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
