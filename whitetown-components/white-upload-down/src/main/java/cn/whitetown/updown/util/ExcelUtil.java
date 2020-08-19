package cn.whitetown.updown.util;

import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import com.alibaba.excel.EasyExcel;
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

    private ExcelUtil() {}

    /**
     * 导出文件时为Writer生成OutputStream
     * @param fileName
     * @param response
     * @return
     * @throws Exception
     */
    public static OutputStream getWebOutputStream(String fileName, HttpServletResponse response) {
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
     * 写出数据
     * @param response 响应
     * @param list 数据
     * @param fileName 文件名
     * @param sheetName excel中表名
     * @param headClass 头信息 - 通常为list中entity类
     * @throws IOException
     */
    public static void writeWebExcel(HttpServletResponse response, List<?> list, String fileName,
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

    public static void readWebExcel(MultipartFile file, Class<?> claz) {

//        EasyExcel.read(file.getInputStream(), claz, new UploadDataListener(uploadDAO)).sheet().doRead();
    }
}
