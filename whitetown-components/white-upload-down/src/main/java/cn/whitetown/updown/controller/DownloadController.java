package cn.whitetown.updown.controller;

import cn.whitetown.authea.annotation.WhiteAuthAnnotation;
import cn.whitetown.dogbase.common.util.BaseIDCreateUtil;
import cn.whitetown.updown.demo.ExcelDemo;
import cn.whitetown.updown.util.ExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author taixian
 * @date 2020/08/18
 **/
@Controller
@RequestMapping("/download")
public class DownloadController {

    /**
     * 下载excel测试接口
     */
    @GetMapping("/excels")
    @WhiteAuthAnnotation
    public void excelDownloadDemo(HttpServletResponse response) throws IOException {
        List<ExcelDemo> data = createData();
        ExcelUtil.getInstance().writeWebExcel(response,data,"demo","demo01",ExcelDemo.class);
    }

    private List<ExcelDemo> createData() {

        List<ExcelDemo> excelDemos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ExcelDemo excelDemo = new ExcelDemo();
            excelDemo.setId(BaseIDCreateUtil.getId());
            excelDemo.setUsername("haha");
            excelDemo.setTelephone("23212");
            excelDemo.setCreateTime(new Date());
            excelDemos.add(excelDemo);
        }
        return excelDemos;
    }

}
