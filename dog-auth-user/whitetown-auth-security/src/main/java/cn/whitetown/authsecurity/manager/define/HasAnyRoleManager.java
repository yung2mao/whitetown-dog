package cn.whitetown.authsecurity.manager.define;

import cn.whitetown.authsecurity.manager.AbstractWhiteUriAuthManager;

/**
 * 有任一角色可访问处理
 * @author taixian
 * @date 2020/07/24
 **/
public class HasAnyRoleManager extends AbstractWhiteUriAuthManager {

    @Override
    public boolean configurePathAuth(String[] paths, String... authors) {
        if(!checkSource(paths,authors)) {
            return false;
        }
        AbstractWhiteUriAuthManager.authorizeRequests.antMatchers(paths).hasAnyRole(authors);
        super.uriAuth2Cache(paths,authors);
        return true;
    }
}
