package cn.whitetown.authea.manager.define;

import cn.whitetown.authea.manager.AbstractWhiteUriAuthManager;

/**
 * 禁止访问处理
 * @author taixian
 * @date 2020/07/24
 **/
public class NoAuthManager extends AbstractWhiteUriAuthManager {

    @Override
    public boolean configurePathAuth(String[] paths, String... authors) {
        if(paths == null || paths.length == 0) {
            return false;
        }
        AbstractWhiteUriAuthManager.authorizeRequests.antMatchers(paths).denyAll();
        return true;
    }
}
