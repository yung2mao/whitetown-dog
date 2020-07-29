package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.authcommon.entity.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户信息缓存工具类
 * @author GrainRain
 * @date 2020/06/18 09:48
 **/
public class WhiteUserCacheUtil implements UserCacheUtil {

    private WhiteExpireMap expireMap;

    @Autowired
    public void setExpireMap(WhiteExpireMap expireMap) {
        this.expireMap = expireMap;
    }

    @Override
    public LoginUser saveLoginUser(String key, LoginUser info){
        Object o = expireMap.putS(key, info, AuthConstant.USER_SAVE_TIME);
        return this.returnLoginUser(o);
    }

    @Override
    public LoginUser getLoginUser(String key) {
        Object o = expireMap.get(key);
        return this.returnLoginUser(o);
    }

    @Override
    public LoginUser removeLoginUser(String key){
        Object o = expireMap.remove(key);
        return this.returnLoginUser(o);
    }

    @Override
    public String saveCaptcha(String sessionId, String captchaText) {
        Object o = expireMap.putS(sessionId, captchaText, AuthConstant.CAPTCHA_EXPIRE_TIME);
        return !(o instanceof String) ? null : String.class.cast(o);
    }

    @Override
    public String getCaptcha(String sessionId) {
        Object o = expireMap.get(sessionId);
        return !(o instanceof String) ? null : String.class.cast(o);
    }

    private LoginUser returnLoginUser(Object o) {
        return !(o instanceof LoginUser) ? null :LoginUser.class.cast(o);
    }
}
