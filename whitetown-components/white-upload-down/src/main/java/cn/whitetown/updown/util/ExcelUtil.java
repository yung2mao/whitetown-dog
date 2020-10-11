package cn.whitetown.updown.util;

import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.updown.manager.ExcelReadManager;
import cn.whitetown.updown.manager.wiml.AsyncExcelListener;
import cn.whitetown.updown.modo.ExcelReadMap;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
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

    /**
     * 写出文件到本地
     * @param path
     * @param headClass
     * @param sheetName
     * @param data
     */
    public void writeLocalExcel(String path, Class<?> headClass, String sheetName, List<?> data) {
        ExcelWriter excelWriter = EasyExcel.write(path).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).head(headClass).build();
        try {
            excelWriter.write(data,writeSheet);
        }finally {
            excelWriter.finish();
        }
    }


    /**
     * 写出数据到web
     * @param response 响应
     * @param data 数据
     * @param fileName 文件名
     * @param sheetName excel中表名
     * @param headClass 头信息 - 通常为list中entity类
     * @throws IOException
     */
    public void writeWebExcel(HttpServletResponse response, List<?> data, String fileName,
                                     String sheetName, Class<?> headClass) throws IOException {
        String fileType = ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(getWebOutputStream(fileName+fileType,response)).build();
        ExcelWriterSheetBuilder sheetBuilder = EasyExcel.writerSheet(sheetName);
        if(headClass != null) {
            sheetBuilder.head(headClass);
        }
        //ignore field
        Class<?> dataClass = data.get(0) == null ? headClass : data.get(0).getClass();
        List<String> excludeFields = this.getIgnoredCol(dataClass, headClass);
        //write to web
        WriteSheet writeSheet = sheetBuilder.excludeColumnFiledNames(excludeFields).build();
        try {
            excelWriter.write(data,writeSheet);
        }catch (Exception e) {
            // reset response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ResponseData fail = ResponseData.fail(ResponseStatusEnum.DOWN_FILE_ERROR);
            response.getWriter().println(JSON.toJSONString(fail));
        }finally {
            excelWriter.finish();
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
    static OutputStream getWebOutputStream(String fileName,HttpServletResponse response) {
        try{
            fileName = URLEncoder.encode(fileName,"utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
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

    /**
     * 获取需要被忽略的字段信息
     * @param dataClass
     * @param templateClass
     * @return
     */
    private List<String> getIgnoredCol(Class<?> dataClass, Class<?> templateClass) {
        if(dataClass == templateClass) {
            return new ArrayList<>();
        }
        Field[] fields = dataClass.getDeclaredFields();
        List<String> excludeFields = new LinkedList<>();
        for(Field field : fields) {
            boolean isIgnore = true;
            try {
                Field headField = templateClass.getDeclaredField(field.getName());
                ExcelIgnore ignore = headField.getAnnotation(ExcelIgnore.class);
                isIgnore = ignore != null;
            }catch (Exception ignored) {}
            if(isIgnore) {
                excludeFields.add(field.getName());
            }
        }
        return excludeFields;
    }
}
