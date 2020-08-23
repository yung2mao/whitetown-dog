package cn.whitetown.updown.manager.wiml;

import java.util.List;
import java.util.Map;

/**
 * 直接输出list结果到控制台的实现
 * @author taixian
 * @date 2020/08/21
 **/
public class ConsoleBaseExcelReadManager extends BaseExcelReadManager {

    @Override
    public void dataDeal(List<Map<Integer,Object>> dataList) {
        System.out.println(dataList);
    }
}
