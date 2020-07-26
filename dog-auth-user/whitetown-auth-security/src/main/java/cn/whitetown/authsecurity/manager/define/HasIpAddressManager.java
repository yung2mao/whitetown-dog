package cn.whitetown.authsecurity.manager.define;

import cn.whitetown.authsecurity.manager.AbstractWhiteUriAuthManager;

/**
 * 指定IP可访问
 * @author taixian
 * @date 2020/07/24
 **/
public class HasIpAddressManager extends AbstractWhiteUriAuthManager {

    @Override
    public boolean configurePathAuth(String[] paths, String... ips) {
        if(!checkSource(paths,ips)) {
            return false;
        }
        AbstractWhiteUriAuthManager.authorizeRequests.antMatchers(paths).hasIpAddress(ips[0]);
        super.uriAuth2Cache(paths,ips);
        return true;
    }
}
