package cn.whitetown.usersingle.controller;

import cn.whitetown.dogbase.common.entity.vo.ResponseData;
import cn.whitetown.dogbase.common.entity.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.user.captcha.CaptchaDataDeal;
import cn.whitetown.dogbase.user.entity.vo.LoginUser;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.usersingle.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录管理
 * @author GrainRain
 * @date 2020/05/26 22:29
 **/
@RestController
@RequestMapping("/erus")
public class CookieLoginController implements LoginController{
    @Autowired
    private LoginService loginService;

    @Autowired
    protected CaptchaDataDeal captchaDataDeal;

    @PostMapping("/login")
    public ResponseData login(String username, String password,HttpServletRequest request,HttpServletResponse response){
        if(DataCheckUtil.checkTextNullBool(username) || DataCheckUtil.checkTextNullBool(password)){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        String token = loginService.checkUsernameAndPassword(username,password);
        WebUtil.addCookie(AuthConstant.TOKEN_COOKIE_NAME,token,AuthConstant.TOKEN_EXPIRE);
        return ResponseData.ok();
    }

    /**
     * 获取当前登录用户的信息
     * @param request
     * @return
     */
    @Override
    @RequestMapping(value = "/getuser",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseData<LoginUser> getLoginUserInfo(HttpServletRequest request) {
        String token = WebUtil.getCookieValue(AuthConstant.TOKEN_COOKIE_NAME, request);
        if(token == null){
            return ResponseData.fail(ResponseStatusEnum.NOT_LOGIN);
        }
        LoginUser user = loginService.getUserInfo(token);
        return ResponseData.ok(user);
    }

    /**
     * 校验验证码 - 根据sessionId确定会话
     * @param captcha
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/check-capt",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseData checkCaptcha(String captcha, HttpServletRequest request){
        String sessionId = WebUtil.getCookieValue(AuthConstant.SESSION_COOKIE_NAME,request);
        String realCaptcha = null;
        if(sessionId !=null){
            realCaptcha = captchaDataDeal.getCaptcha(sessionId);
        }else {
            //已过期
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_EXPIRE);
        }
        //已过期
        if(realCaptcha == null || "".equals(realCaptcha)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_EXPIRE);
        }
        //验证码错误
        if(!realCaptcha.equalsIgnoreCase(captcha)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
        return ResponseData.ok();
    }

    /**
     * 生成验证码
     */
    @RequestMapping("/ver")
    public void createCaptcha(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaDataDeal.createCaptchaText();

        // store the text
        String sessionId = session.getId();
        captchaDataDeal.saveCaptcha(sessionId,capText);

        //add cookie
        Cookie cookie = new Cookie(AuthConstant.SESSION_COOKIE_NAME,sessionId);
        cookie.setPath(request.getContextPath()+"/");
        cookie.setMaxAge(180);
        response.addCookie(cookie);

        // create the image with the text
        BufferedImage bi = captchaDataDeal.createCaptchaImage(capText);
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
    }
}
