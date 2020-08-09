package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.util.captcha.CaptchaDataDeal;
import cn.whitetown.authea.annotation.WhiteAuthAnnotation;
import cn.whitetown.authea.modo.WhiteControlType;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.authcommon.entity.dto.LoginUser;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.logclient.modo.WhLogConstants;
import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.usersecurity.service.DogUserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
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
@WhiteAuthAnnotation(type = WhiteControlType.AUTHENTICATED)
public class DogUserController {

    private Logger log = WhLogConstants.opLogger;

    @Autowired
    private CaptchaDataDeal captchaDataDeal;

    @Autowired
    private DogUserService userService;
    /**
     * 生成验证码
     * @param response
     */
    @GetMapping("/ver")
    @WhiteAuthAnnotation
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
        // store the text and sessionId
        String sessionId = WebUtil.getCusSessionId(request);
        captchaDataDeal.saveCaptcha(sessionId,captchaText.toLowerCase());
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
        log.debug("验证码生成成功，客户端IP地址为>"+WebUtil.getClientIP(request)+", 验证码为>"+captchaText);
    }

    /**
     * 校验验证码
     * @param request
     * @param captchaJson
     * @return
     */
    @PostMapping(value = "check_capt",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation
    public ResponseData checkCaptcha(@RequestBody JSONObject captchaJson, HttpServletRequest request){
        String captcha = captchaJson.getString("captcha");
        String sessionId = WebUtil.getCusSessionId(request);

        userService.checkCaptcha(captcha,sessionId);
        log.debug("验证码校验通过，当前用户IP地址为 >>" + WebUtil.getClientIP(request));
        return ResponseData.ok();
    }

    /**
     * 登录操作
     * @param params
     * @return
     */
    @PostMapping(value = "/login",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation
    public ResponseData<String> dogLogin(@RequestBody JSONObject params,HttpServletRequest request){
        String captcha = params.getString("captcha");
        if(DataCheckUtil.checkTextNullBool(captcha)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
        String sessionId = WebUtil.getCusSessionId(request);
        userService.checkCaptcha(captcha,sessionId);
        //check username and password
        String username = params.getString("username");
        String password = params.getString("password");

        if(DataCheckUtil.checkTextNullBool(username) || DataCheckUtil.checkTextNullBool(password)){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        String token = userService.checkUserNameAndPassword(username,password);
        return ResponseData.ok(token);
    }

    /**
     * token更新，解析原有token，签发一个新的token
     * @return
     */
    @GetMapping("/new_token")
    public ResponseData<String> updateToken(){
        String token = userService.updateToken();
        return ResponseData.ok(token);
    }

    /**
     * 退出登录操作
     * @return
     */
    @RequestMapping(value = "/logout",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseData dogLogout(){
        userService.logout();
        return ResponseData.ok();
    }

    /**
     * 获取当前登录用户的基本信息
     * @return
     */
    @GetMapping("/info")
    public ResponseData<LoginUser> getUserInfo(){
        LoginUser user = userService.getUserByToken();
        user.setRoles(null);
        return ResponseData.ok(user);
    }
}
