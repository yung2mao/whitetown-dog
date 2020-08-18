package cn.whitetown.updown.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel Util
 * @author taixian
 * @date 2020/08/18
 **/
public class ExcelUtil {

    private ExcelUtil() {}

    /**
     * 导出文件时为Writer生成OutputStream
     * @param fileName
     * @param response
     * @return
     * @throws Exception
     */
    public static OutputStream getOutputStream(String fileName, HttpServletResponse response)
            throws Exception{
        try{
            fileName = URLEncoder.encode(fileName,"utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e){
            throw new Exception("导出文件失败！");
        }
    }

    /**
     * 写出数据
     * @param response
     * @param list
     * @param fileName
     * @param sheetName
     * @throws Exception
     */
    public static void writeExcel(HttpServletResponse response, List<?> list, String fileName,
                                  String sheetName, Class<?> headClass) throws Exception {
        ExcelWriter writer = null;
        if(headClass != null) {
            writer = EasyExcel.write(getOutputStream(fileName, response), headClass).build();
        }else {
            writer = EasyExcel.write(getOutputStream(fileName, response)).build();
        }
        WriteSheet writeSheet = new WriteSheet();
        writeSheet.setSheetName(sheetName);
        writer.write(list,writeSheet);
        writer.finish();
    }
}
