package cn.whitetown;

import cn.whitetown.updown.manager.wiml.AsyncExcelReadManager;
import cn.whitetown.updown.util.ExcelUtil;
import org.junit.Test;

/**
 * @author taixian
 * @date 2020/08/20
 **/
public class ExcelTest {

    @Test
    public void readExcel() {
        String path = "E:\\work\\document\\集成平台接口对接\\数据字典\\统计系统彩超.xlsx";
        ExcelUtil excelUtil = ExcelUtil.getInstance();
        excelUtil.readLocalExcel(path, AsyncExcelReadManager.getInstance());
    }
}
