package cn.whitetown.monitor.sys.server.wiml;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.dao.DataSourceUtil;
import cn.whitetown.monitor.dao.ShardingDataSourceUtil;
import cn.whitetown.monitor.sys.modo.dto.WhMonSaveParam;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.monitor.sys.server.MonitorDao;
import cn.whitetown.monitor.sys.server.config.MonServerConfig;
import cn.whitetown.monitor.util.MonitorIDUtil;
import com.alibaba.fastjson.JSON;

import java.sql.Connection;
import java.util.List;

/**
 * 监控数据存储处理器
 * @author taixian
 * @date 2020/08/08
 **/
public class MonScopeSaveManager implements MonitorDao {

    private static DataSourceUtil dataSourceUtil = ShardingDataSourceUtil.DS_UTIL;

    private static String baseTable = MonConfConstants.BASE_TABLE_NAME;

    private MonitorIDUtil idUtil;

    public MonScopeSaveManager() {
        idUtil = MonServerConfig.CONFIG.monitorIDUtil();
    }

    @Override
    public void save(WhiteMonitorParams whiteMonitorParams) {
        long newId = idUtil.getId();
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

    @Override
    public WhiteMonitorParams getRecent(String serverId) {
        return null;
    }

    @Override
    public List<WhiteMonitorParams> getAll(String serverId) {
        return null;
    }
}
