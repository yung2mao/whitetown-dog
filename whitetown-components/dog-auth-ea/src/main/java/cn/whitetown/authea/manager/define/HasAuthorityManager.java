package cn.whitetown.authea.manager.define;

import cn.whitetown.authea.manager.AbstractWhiteUriAuthManager;

/**
 * 有对应权限标识可访问处理
 * @author taixian
 * @date 2020/07/24
 **/
public class HasAuthorityManager extends AbstractWhiteUriAuthManager {

    @Override
    public boolean configurePathAuth(String[] paths, String... authors) {
        if(checkSource(paths,authors)) {
            return false;
        }
        AbstractWhiteUriAuthManager.authorizeRequests.antMatchers(paths).hasAuthority(authors[0]);
        super.uriAuth2Cache(paths,authors[0]);
        return true;
    }
}
