package cn.whitetown.updown.util;

import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.updown.manager.ExcelReadManager;
import cn.whitetown.updown.manager.wiml.AsyncExcelListener;
import cn.whitetown.updown.modo.ExcelReadMap;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import org.springframework.web.multipart.MultipartFile;

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

    private static ExcelUtil EXCEL_UTIL;

    private ExcelUtil() {}

    public static ExcelUtil getInstance() {
        if(EXCEL_UTIL == null) {
            EXCEL_UTIL = new ExcelUtil();
        }
        return EXCEL_UTIL;
    }

    /**
     * 读取本地文件 - 指定表头
     * 默认读取sheet 0
     * @param path
     * @param entityClass
     */
    public <T> void readLocalExcel(String path, Class<?> entityClass, ExcelReadManager<T> readManager) {
        ExcelReader excelReader = EasyExcel.read(path, entityClass, new AsyncExcelListener<>(readManager)).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        this.readExcel(excelReader,readSheet);
    }

    /**
     * 读取本地文件 - 不指定表头
     * 默认读取sheet0
     * @param pathName
     * @param readManager
     */
    public <T> void readLocalExcel(String pathName,ExcelReadManager<T> readManager) {
        ExcelReader excelReader = EasyExcel.read(pathName, new AsyncExcelListener<>(readManager)).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        this.readExcel(excelReader,readSheet);
    }

    /**
     * 读取本地文件
     * 读取多个sheet
     * @param path
     * @param excelReadMaps
     */
    public void readLocalExcel(String path, ExcelReadMap ... excelReadMaps) {
        if(excelReadMaps == null || excelReadMaps.length == 0) {
            return;
        }
        ExcelReader excelReader = EasyExcel.read(path).build();
        ReadSheet[] readSheets = new ReadSheet[excelReadMaps.length];
        for (int i = 0; i < excelReadMaps.length; i++) {
            ExcelReadMap excelReadMap = excelReadMaps[i];
            if(excelReadMap.getSheetNo() == null && excelReadMap.getSheetName() == null) {
                continue;
            }
            ExcelReaderSheetBuilder sheetBuilder = EasyExcel.readSheet();
            if(excelReadMap.getSheetNo() != null) {
                sheetBuilder.sheetNo(excelReadMap.getSheetNo());
            }else {
                sheetBuilder.sheetName(excelReadMap.getSheetName());
            }
            if(excelReadMap.getSheetClass() != null) {
                sheetBuilder.head(excelReadMap.getSheetClass());
            }
            if(excelReadMap.getHeadRowNumber() != null) {
                sheetBuilder.headRowNumber(excelReadMap.getHeadRowNumber());
            }
            ReadSheet readSheet = sheetBuilder
                    .registerReadListener(new AsyncExcelListener<>(excelReadMap.getReadManager()))
                    .build();
            readSheets[i] = readSheet;
        }
        this.readExcel(excelReader,readSheets);
    }

    /**
     * 读取web上传的excel
     * 读取单个sheet
     * @param file
     * @param claz
     */
    public void readWebExcel(MultipartFile file, Class<?> claz, Integer sheetNumber, ExcelReadManager readManager) throws IOException {
        ExcelReader excelReader = EasyExcel.read(file.getInputStream(), claz, new AsyncExcelListener(readManager)).build();
        sheetNumber = (sheetNumber == null && sheetNumber >= 0) ? 0 : sheetNumber;
        ReadSheet readSheet = EasyExcel.readSheet(sheetNumber).build();
        this.readExcel(excelReader,readSheet);
    }


    public void writeLocalExcel() {

    }


    /**
     * 写出数据到web
     * @param response 响应
     * @param list 数据
     * @param fileName 文件名
     * @param sheetName excel中表名
     * @param headClass 头信息 - 通常为list中entity类
     * @throws IOException
     */
    public void writeWebExcel(HttpServletResponse response, List<?> list, String fileName,
                                     String sheetName, Class<?> headClass) throws IOException {
        try {
            if(headClass != null) {
                EasyExcel.write(getWebOutputStream(fileName,response), headClass).sheet(sheetName).doWrite(list);
            }else {
                EasyExcel.write(getWebOutputStream(fileName,response)).sheet(sheetName).doWrite(list);
            }
        }catch (Exception e) {
            // reset response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ResponseData fail = ResponseData.fail(ResponseStatusEnum.DOWN_FILE_ERROR);
            response.getWriter().println(JSON.toJSONString(fail));
        }
        response.flushBuffer();
    }

    /**
     * 导出文件时为Writer生成OutputStream
     * @param fileName
     * @param response
     * @return
     * @throws Exception
     */
    private OutputStream getWebOutputStream(String fileName, HttpServletResponse response) {
        try{
            fileName = URLEncoder.encode(fileName,"utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 读取excel ,多 sheet
     * @param excelReader
     * @param readSheet
     */
    private void readExcel(ExcelReader excelReader, ReadSheet ... readSheet) {
        try {
            excelReader.read(readSheet);
        }finally {
            if(excelReader != null) {
                excelReader.finish();
            }
        }
    }
}
