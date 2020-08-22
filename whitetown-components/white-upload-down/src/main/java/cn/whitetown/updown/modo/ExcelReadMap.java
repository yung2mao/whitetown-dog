package cn.whitetown.updown.modo;

import cn.whitetown.updown.manager.ExcelReadManager;
import lombok.Getter;
import lombok.Setter;

/**
 * excel读所需信息
 * @author taixian
 * @date 2020/08/20
 **/
@Setter
@Getter
public class ExcelReadMap {
    private Integer sheetNo;
    private String sheetName;
    private Integer headRowNumber;
    private Class<?> sheetClass;
    private ExcelReadManager<?> readManager;

    public ExcelReadMap() {
    }

    public ExcelReadMap(Integer sheetNo, String sheetName, Integer headRowNumber,
                        Class<?> sheetClass, ExcelReadManager<?> readManager) {
        this.sheetNo = sheetNo;
        this.sheetName = sheetName;
        this.headRowNumber = headRowNumber;
        this.sheetClass = sheetClass;
        this.readManager = readManager;
    }
}
