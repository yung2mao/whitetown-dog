package cn.whitetown;

import cn.hutool.core.thread.ThreadUtil;
import cn.whitetown.updown.manager.wiml.ConsoleBaseExcelReadManager;
import cn.whitetown.updown.modo.ExcelReadMap;
import cn.whitetown.updown.util.ExcelUtil;
import org.junit.Test;
import java.util.concurrent.CountDownLatch;

/**
 * @author taixian
 * @date 2020/08/20
 **/
public class ExcelTest {

//    @Test
    public void readExcel() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExcelReadMap excelReadMap = new ExcelReadMap();
        excelReadMap.setSheetNo(0);
        excelReadMap.setReadManager(new ConsoleBaseExcelReadManager());
        String path = "E:\\work\\document\\集成平台接口对接\\数据字典\\数据字典THIS4（20181221）.xlsx";
        ExcelUtil excelUtil = ExcelUtil.getInstance();
        ThreadUtil.execute(()->excelUtil.readLocalExcel(path, excelReadMap));
        countDownLatch.await();
    }
}
