package cn.whitetown.authea.manager.define;

import cn.whitetown.authea.manager.AbstractWhiteUriAuthManager;

/**
 * 有指定角色可访问处理
 * @author taixian
 * @date 2020/07/24
 **/
public class HasRoleManager extends AbstractWhiteUriAuthManager {

    @Override
    public boolean configurePathAuth(String[] paths, String... authors) {
        if(!checkSource(paths,authors)) {
            return false;
        }
        AbstractWhiteUriAuthManager.authorizeRequests.antMatchers(paths).hasRole(authors[0]);
        super.uriAuth2Cache(paths,authors[0]);
        return true;
    }

    @Override
    public String[] getPathAuthors(String path) {
        String[] pathAuthors = super.getPathAuthors(path);
        for (int i = 0; i < pathAuthors.length; i++) {
            if(!pathAuthors[i].startsWith("ROLE_")) {
                pathAuthors[i] = "ROLE_" + pathAuthors[i];
            }
        }
        return pathAuthors;
    }
}
