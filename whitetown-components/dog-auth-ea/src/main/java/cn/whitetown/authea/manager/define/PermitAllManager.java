package cn.whitetown.authea.manager.define;

import cn.whitetown.authea.manager.AbstractWhiteUriAuthManager;

/**
 * 所有权限均可访问处理
 * @author taixian
 * @date 2020/07/24
 **/
public class PermitAllManager extends AbstractWhiteUriAuthManager {

    @Override
    public boolean configurePathAuth(String[] paths, String... authors) {
        if(paths == null || paths.length == 0){
            return false;
        }
        AbstractWhiteUriAuthManager.authorizeRequests.antMatchers(paths).permitAll();
        return true;
    }

    @Override
    public String[] getPathAuthors(String path) {
        return new String[0];
    }
}
