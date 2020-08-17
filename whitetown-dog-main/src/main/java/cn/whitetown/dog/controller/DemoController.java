package cn.whitetown.dog.controller;

import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.esconfig.manager.EsSearchManager;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GrainRain
 * @date 2020/05/31 12:20
 **/
@Controller
public class DemoController {
//    @RequestMapping("/test01")
    public String test01(HttpServletRequest request){
        request.setAttribute("user","fsdfsg");
        return "login/login.html";
    }

    @ResponseBody
    @GetMapping("/us/{pa}")
    public String getParam(@PathVariable("pa") String param) {
        return "success";
    }
}
