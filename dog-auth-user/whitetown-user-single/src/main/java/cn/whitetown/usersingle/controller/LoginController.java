package cn.whitetown.usersingle.controller;

import cn.whitetown.dogbase.domain.vo.ResponseData;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author GrainRain
 * @date 2020/05/27 21:47
 **/
public interface LoginController {

    ResponseData checkLogin(HttpServletRequest request, HttpServletResponse response);

    String login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    ResponseData checkCaptcha(String captcha, HttpServletRequest request);

    void createCaptcha(HttpServletRequest request, HttpServletResponse response);

    ResponseData<UserBasicInfo> getLoginUserInfo(HttpServletRequest request);
}
