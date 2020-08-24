package cn.whitetown.smartxml.demo.controller;

import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.smartxml.demo.entity.DemoSubject;
import cn.whitetown.smartxml.manager.XmlAutoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * 解析和封装xml示例
 * 本例为简化测试,采用rest方式提供测试类
 * 本组件适用于复杂嵌套类型xml解析与封装,例如hl7标准的xml文档
 * 而简单结构的xml文档无需以此方式进行
 * 对于webservice类型传参,可以直接封装成element对象类型
 * @author taixian
 * @date 2020/08/24
 **/
@RestController
@RequestMapping("/xml")
public class ReadAndWriteController {

    /**
     * 为了简化,此处直接在controller层处理
     */
    @Autowired
    private XmlAutoUtil xmlUtil;

    /**
     * 读取xml为对象
     * 只需指定类型即可完成
     * @param xml
     * @return
     */
    @PostMapping("/demo/reads")
    public ResponseData readXml(@RequestBody @NotBlank String xml) {
        try {
            DemoSubject obj = xmlUtil.readXmlAsObj(xml, DemoSubject.class);
            System.out.println(obj);
            return ResponseData.ok(obj);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseData.build(400,e.getMessage(),null);
        }
    }

    /**
     * 将对象封装为xml - 直接传入对象即可完成
     * @return
     */
    @PostMapping("/demo/writes")
    public ResponseData writeXml() {
        try {
            String xml = xmlUtil.getXmlByObjAndModel(getData());
            return ResponseData.ok(xml);
        }catch (Exception e) {
            return ResponseData.build(400,e.getMessage(),null);
        }
    }

    public DemoSubject getData() {
        DemoSubject demoSubject = new DemoSubject();
        demoSubject.setAssignCode("4566565");
        demoSubject.setAssignDepartmentCode("67w987");
        demoSubject.setBirthTime("19910423");
        demoSubject.setDepartmentName("hellow");
        return demoSubject;
    }
}
