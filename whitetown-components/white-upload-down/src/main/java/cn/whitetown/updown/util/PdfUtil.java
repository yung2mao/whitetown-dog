package cn.whitetown.updown.util;

import cn.whitetown.updown.annotation.PdfCellIgnore;
import cn.whitetown.updown.annotation.PdfCellProperty;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

/**
 * pdf util
 * @author taixian
 * @date 2020/08/23
 **/
public class PdfUtil {

    private static PdfUtil pdfUtil;

    private PdfUtil() {
    }

    public static PdfUtil getInstance() {
        if(pdfUtil == null) {
            pdfUtil = new PdfUtil();
        }
        return pdfUtil;
    }

    /**
     * 写出pdf到本地
     * @param path
     * @param dataList
     * @param templateClass
     */
    public void defaultWritePdf(String path, List<?> dataList, Class<?> templateClass) throws IOException, DocumentException {
        File file = new File(path);
        FileOutputStream out = new FileOutputStream(file);
        this.defaultWritePdf(out,dataList,templateClass);
    }

    /**
     * 写出pdf
     * @param response
     * @param fileName
     * @param dataList
     * @param templateClass
     * @throws IOException
     * @throws DocumentException
     */
    public void defaultWriteWebPdf(HttpServletResponse response, String fileName, List<?> dataList, Class<?> templateClass) throws IOException, DocumentException {
        OutputStream outputStream = ExcelUtil.getWebOutputStream(fileName, response);
        this.defaultWritePdf(outputStream, dataList, templateClass);
    }

    private void defaultWritePdf(OutputStream out, List<?> dataList, Class<?> templateClass) throws IOException, DocumentException {
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        //单元格信息
        Font font = new Font(baseFont,10);
        //A4尺寸
        Document document = new Document(new RectangleReadOnly(842.0F, 595.0F));
        PdfWriter.getInstance(document, out);
        document.open();
        Field[] fields = templateClass.getDeclaredFields();
        Map<String,Field> fieldMap = new LinkedHashMap<>();
        //收集需要写出的列信息
        Arrays.stream(fields).forEach(field -> {
            PdfCellIgnore ignore = field.getAnnotation(PdfCellIgnore.class);
            if(ignore != null) {
                return;
            }
            String colName = null;
            PdfCellProperty cellAnnotation = field.getAnnotation(PdfCellProperty.class);
            if(cellAnnotation != null && !"".equals(cellAnnotation.value())){
                colName = cellAnnotation.value();
            }else {
                colName = field.getName();
            }
            fieldMap.put(colName,field);
        });
        //创建table并设置表头
        PdfPTable table = new PdfPTable(fieldMap.size());
        fieldMap.keySet().stream().forEach(colName->{
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setPhrase(new Paragraph(colName,font));
            table.addCell(pdfPCell);
        });
        //添加数据到table中
        dataList.forEach(obj->{
            Set<String> cols = fieldMap.keySet();
            cols.forEach(col->{
                Field field = fieldMap.get(col);
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                value = value == null ? "" : value;
                PdfPCell pdfPCell = new PdfPCell();
                pdfPCell.setPhrase(new Paragraph(String.valueOf(value),font));
                table.addCell(pdfPCell);
            });
        });
        //写出数据
        document.add(table);
        document.close();
    }
}
