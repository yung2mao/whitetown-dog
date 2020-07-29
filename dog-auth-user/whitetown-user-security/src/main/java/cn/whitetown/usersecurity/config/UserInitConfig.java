package cn.whitetown.usersecurity.config;

import cn.whitetown.authcommon.util.*;
import cn.whitetown.authcommon.util.captcha.CaptchaBasicInfo;
import cn.whitetown.authcommon.util.captcha.CaptchaDataDeal;
import cn.whitetown.authcommon.util.captcha.DefaultCaptchaDataDeal;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.util.defaultimpl.DefaultDeptUtil;
import cn.whitetown.authcommon.util.defaultimpl.DefaultMenuCacheUtil;
import cn.whitetown.authcommon.util.defaultimpl.DefaultMenuUtil;
import cn.whitetown.authcommon.util.defaultimpl.WhiteJwtTokenUtil;
import cn.whitetown.authcommon.util.defaultimpl.WhiteUserCacheUtil;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import cn.whitetown.usersecurity.util.define.WhiteAuthUserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author GrainRain
 * @date 2020/06/13 11:30
 **/
@Configuration
public class UserInitConfig {

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
    public JwtTokenUtil jwtTokenUtil(){
        return new WhiteJwtTokenUtil(AuthConstant.TOKEN_SECRET,
                AuthConstant.TOKEN_EXPIRE,
                AuthConstant.TOKEN_PREFIX,
                AuthConstant.HEADER_STRING);
    }

    /**
     * 初始化用户权限信息缓存工具类
     * @return
     */
    @Bean
    public AuthUserCacheUtil authUserCacheUtil(){
        return new WhiteAuthUserCache(userCacheUtil);
    }

    /**
     * userCache初始化
     */
    @Autowired
    private UserCacheUtil userCacheUtil;
    @Bean
    public UserCacheUtil userCacheUtil() {
        return new WhiteUserCacheUtil();
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
