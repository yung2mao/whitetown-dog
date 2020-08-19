package cn.whitetown.monitor.sys.manager.wiml;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监控信息保存管理 - 保存到文件系统
 * @author taixian
 * @date 2020/08/01
 **/
public class WhiteMonFileSaveManager implements MonitorInfoSaveManager {

    private static MonitorInfoSaveManager monitorInfoSaveManager = new WhiteMonFileSaveManager();

    private Logger logger = LogConstants.SYS_LOGGER;

    private String basePath;

    private AtomicInteger retryTimes;

    private String separator = System.getProperty("file.separator");

    private OutputStream outputStream;

    private WhiteMonFileSaveManager() {
    }

    public static MonitorInfoSaveManager getInstance() {
        return monitorInfoSaveManager;
    }

    @Override
    public void init() throws IOException {
        if(MonConfConstants.LOG_SAVE_FAIL_TRY) {
            retryTimes = new AtomicInteger(MonConfConstants.RETRY_TIMES);
        }else {
            retryTimes = new AtomicInteger(0);
        }
        String logSavePath = MonConfConstants.LOG_SAVE_PATH;
        String cusSeparator = "/";
        if(logSavePath == null || logSavePath.equals(cusSeparator) || logSavePath.equals(separator)) {
            basePath = MonConfConstants.PROJECT_DIR;
        }else {
            basePath = logSavePath;
        }
        String docPath = basePath + separator + this.createDocName();
        boolean fileStatus = this.checkOrCreateDoc(basePath);
        if(!fileStatus) {
            throw new IOException("文件系统异常");
        }
        outputStream = new FileOutputStream(docPath,true);
    }

    @Override
    public boolean save(WhiteMonitorParams monitorParams) {
        if(!this.checkOrCreateDoc(basePath + separator + this.createDocName())) {
            return false;
        }
        boolean isWrite = this.write(monitorParams);
        while (!isWrite) {
            if(retryTimes.getAndDecrement() > 0) {
                this.checkOrCreateDoc(basePath + separator + this.createDocName());
                isWrite = this.write(monitorParams);
            }else {
                break;
            }
        }
        retryTimes.set(MonConfConstants.RETRY_TIMES);
        return isWrite;
    }

    @Override
    public void destroy() {
        if(outputStream == null) {
            return;
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            outputStream = null;
        }
    }

    /**
     * 构建文档名称
     * @return
     */
    private String createDocName() {
        StringBuilder builder = new StringBuilder(MonConfConstants.WORK_ID);
        LocalDateTime now = LocalDateTime.now();
        builder.append("_")
                .append(now.getYear()).append("-")
                .append(now.getMonth().getValue()).append("-")
                .append(now.getDayOfMonth())
                .append(".log");
        return builder.toString();
    }

    /**
     * 检查log文件是否已存在,不存在则创建
     * @param path
     * @return
     */
    private boolean checkOrCreateDoc(String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
                this.resetOutputStream(file);
            }
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean write(WhiteMonitorParams monitorParams) {
        if(monitorParams == null) {
            return false;
        }
        if(outputStream == null) {
            return false;
        }
        String monitorLog = monitorParams.toString() + MonConfConstants.LINE_SEPARATOR;
        byte[] bytes = monitorLog.getBytes();
        try {
            outputStream.write(bytes);
        }catch (NullPointerException nullException) {
        }catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    private void resetOutputStream(File file) throws FileNotFoundException {
        this.destroy();
        outputStream = new FileOutputStream(file,true);
    }
}
