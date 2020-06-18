package cn.whitetown.usersecurity.controller;

import cn.whitetown.dogbase.domain.vo.ResponseData;
import cn.whitetown.dogbase.user.captcha.DefaultCaptchaDataDeal;
import cn.whitetown.dogbase.user.entity.LoginUser;
import cn.whitetown.dogbase.util.WebUtil;
import cn.whitetown.usersecurity.service.DogUserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 用户信息处理
 * @author GrainRain
 * @date 2020/06/13 10:25
 **/
@RestController
@RequestMapping("/erus")
public class DogUserController {

    private Log log = LogFactory.getLog(DogUserController.class);

    @Autowired
    private DefaultCaptchaDataDeal captchaDataDeal;

    @Autowired
    private DogUserService userService;
    /**
     * 生成验证码
     * @param response
     */
    @GetMapping("/ver")
    public void createCaptcha(HttpServletRequest request,HttpServletResponse response){

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        String captchaText = captchaDataDeal.createCaptchaText();
        // store the text and ip
        String clientIP = WebUtil.getClientIP(request);
        captchaDataDeal.saveCaptcha(clientIP,captchaText.toLowerCase());

        //write image
        BufferedImage bi = captchaDataDeal.createCaptchaImage(captchaText);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write the data out
        try {
            ImageIO.write(bi, "jpg", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.warn("验证码生成成功，客户端IP地址为>"+clientIP+", 验证码为>"+captchaText);
    }

    /**
     * 校验验证码
     * @param request
     * @param captchaJson
     * @return
     */
    @PostMapping(value = "check-capt",produces = "application/json;charset=UTF-8")
    public ResponseData checkCaptcha(@RequestBody JSONObject captchaJson, HttpServletRequest request){
        String captcha = captchaJson.getString("captcha");
        String clientIp = WebUtil.getClientIP(request);

        userService.checkCaptcha(captcha,clientIp);
        log.warn("验证码校验通过，当前用户IP地址为 >>"+clientIp);
        return ResponseData.ok();
    }

    /**
     * 登录操作
     * @param params
     * @return
     */
    @PostMapping(value = "/login",produces = "application/json;charset=UTF-8")
    public ResponseData<String> login(@RequestBody JSONObject params,HttpServletRequest request){
        String captcha = params.getString("captcha");
        String clientIp = WebUtil.getClientIP(request);
        userService.checkCaptcha(captcha,clientIp);
        //check username and password
        String username = params.getString("username");
        String password = params.getString("password");
        String token = userService.checkUserNameAndPassword(username,password);
        return ResponseData.ok(token);
    }

    /**
     * 退出登录操作
     * @return
     */
    @RequestMapping(value = "/logout",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseData logout(){
        userService.logout();
        return ResponseData.ok();
    }

    /**
     * 获取当前登录用户的基本信息
     * @return
     */
    @GetMapping("/info")
    public ResponseData<LoginUser> getUser(){
        LoginUser user = userService.getUserByToken();
        user.setRoles(null);
        return ResponseData.ok(user);
    }

}
