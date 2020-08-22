package cn.whitetown.updown.manager.wiml;

import cn.whitetown.updown.manager.ExcelReadManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 数据直接以map形式进行处理
 * @author taixian
 * @date 2020/08/20
 **/
public abstract class BaseExcelReadManager implements ExcelReadManager<Map<Integer,Object>> {

    private Log logger = LogFactory.getLog(BaseExcelReadManager.class);

    private boolean isFinish = false;

    @Override
    public List<Map<Integer, Object>> getData() {
        return null;
    }

    @Override
    public boolean isFinish() {
        return isFinish;
    }

    @Override
    public void doAfterAllAnalysed() {
        logger.debug("read sheet finished");
        isFinish = true;
    }
}
