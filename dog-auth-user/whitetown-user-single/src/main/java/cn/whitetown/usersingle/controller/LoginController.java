package cn.whitetown.usersingle.controller;

import cn.whitetown.dogbase.domain.vo.ResponseData;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录处理
 * @author GrainRain
 * @date 2020/05/27 21:47
 **/
public interface LoginController {
    /**
     * 登录状态检查 - //TODO:放到过滤器处理
     * @param request
     * @param response
     * @return
     */
    ResponseData checkLogin(HttpServletRequest request, HttpServletResponse response);

    /**
     * 登录处理
     * @param username 用户名
     * @param password 密码
     * @param request
     * @param response
     * @return
     */
    ResponseData login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    /**
     * 验证码校验
     * @param captcha 验证码字符串
     * @param request
     * @return
     */
    ResponseData checkCaptcha(String captcha, HttpServletRequest request);

    /**
     * 生成验证码
     * @param request
     * @param response
     */
    void createCaptcha(HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取用户基本信息
     * @param request
     * @return
     */
    ResponseData<UserBasicInfo> getLoginUserInfo(HttpServletRequest request);
}
