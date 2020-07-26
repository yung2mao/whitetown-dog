package cn.whitetown.authsecurity.manager.define;

import cn.whitetown.authsecurity.manager.AbstractWhiteUriAuthManager;

/**
 * 有任一权限标识即可访问处理
 * @author taixian
 * @date 2020/07/24
 **/
public class HasAnyAuthorityManager extends AbstractWhiteUriAuthManager {

    @Override
    public boolean configurePathAuth(String[] paths, String... authors) {
        boolean isSuccess = super.configurePathAuth(paths, authors);
        if (!isSuccess) {
            return false;
        }
        super.uriAuth2Cache(paths,authors);
        return true;
    }
}
