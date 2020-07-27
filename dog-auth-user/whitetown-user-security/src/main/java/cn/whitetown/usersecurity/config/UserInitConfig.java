package cn.whitetown.usersecurity.config;

import cn.whitetown.authcommon.util.DeptUtil;
import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.authcommon.util.MenuUtil;
import cn.whitetown.authcommon.util.captcha.CaptchaBasicInfo;
import cn.whitetown.authcommon.util.captcha.CaptchaDataDeal;
import cn.whitetown.authcommon.util.captcha.DefaultCaptchaDataDeal;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.util.defaultimpl.DefaultDeptUtil;
import cn.whitetown.authcommon.util.defaultimpl.DefaultMenuCacheUtil;
import cn.whitetown.authcommon.util.defaultimpl.DefaultMenuUtil;
import cn.whitetown.authcommon.util.token.WhiteJwtTokenUtil;
import cn.whitetown.authcommon.util.defaultimpl.WhiteUserCacheUtil;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import cn.whitetown.usersecurity.util.define.WhiteAuthUserCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author GrainRain
 * @date 2020/06/13 11:30
 **/
@Configuration
public class UserInitConfig {
    /**
     * token密钥
     */
    private String tokenSecret = AuthConstant.TOKEN_SECRET;
    /**
     * token过期时间为7天 - 7天内免登录
     */
    private long tokenExpire = AuthConstant.TOKEN_EXPIRE;

    /**
     * 初始化验证码操作类
     * @return
     */
    @Bean
    public CaptchaDataDeal defaultCaptchaDataDeal(){
        return new DefaultCaptchaDataDeal(new CaptchaBasicInfo());
    }

    /**
     * 初始化token配置
     * @return
     */
    @Bean
    public WhiteJwtTokenUtil jwtTokenUtil(){
        return new WhiteJwtTokenUtil(tokenSecret,tokenExpire);
    }

    /**
     * 初始化用户权限信息缓存工具类
     * @return
     */
    @Bean
    public AuthUserCacheUtil userCacheUtil(){
        return new WhiteAuthUserCache(new WhiteUserCacheUtil());
    }

    /**
     * 初始化菜单管理工具类
     * @return
     */
    @Bean
    public MenuUtil menuUtil(){
        return new DefaultMenuUtil();
    }

    /**
     * 初始化菜单缓存工具
     * @return
     */
    @Bean
    public MenuCacheUtil menuCacheUtil(){
        DefaultMenuCacheUtil menuCacheUtil = new DefaultMenuCacheUtil();
        menuCacheUtil.init();
        return menuCacheUtil;
    }

    /**
     * 初始化部门处理工具类
     * @return
     */
    @Bean
    public DeptUtil deptUtil(){
        return new DefaultDeptUtil();
    }
}
