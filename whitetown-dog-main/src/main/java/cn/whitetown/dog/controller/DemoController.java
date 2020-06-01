package cn.whitetown.dog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GrainRain
 * @date 2020/05/31 12:20
 **/
@Controller
public class DemoController {
    @RequestMapping("/test01")
    public String test01(HttpServletRequest request){
        request.setAttribute("user","fsdfsg");
        return "login/login.html";
    }
}
