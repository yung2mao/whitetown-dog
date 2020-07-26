package cn.whitetown.authsecurity.manager.define;

import cn.whitetown.authsecurity.manager.AbstractWhiteUriAuthManager;

/**
 * 登录即可访问处理
 * @author taixian
 * @date 2020/07/24
 **/
public class AuthenticatedManager extends AbstractWhiteUriAuthManager {

    @Override
    public boolean configurePathAuth(String[] paths, String... authors) {
        if(paths == null || paths.length == 0) {
            return false;
        }
        AbstractWhiteUriAuthManager.authorizeRequests.antMatchers(paths).authenticated();
        return true;
    }

    @Override
    public String[] getPathAuthors(String path) {
        return new String[0];
    }
}
