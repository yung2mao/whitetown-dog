package cn.whitetown.authea.manager;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认实现资源权限配置管理接口
 * @author taixian
 * @date 2020/07/24
 **/
public abstract class AbstractWhiteUriAuthManager implements WhiteUriAuthManager {

    protected static ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests;

    protected static Map<String,String[]> uriAuthorMap = new ConcurrentHashMap<>(32);

    /**
     * 初始化方法 - 初始化authorizeRequests
     * @param reaAuthorizeRequests
     */
    public static void initAuthorizeRequests(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry reaAuthorizeRequests) {
        if(reaAuthorizeRequests == null) {
            throw new NullPointerException("authorizeRequests is null");
        }
        authorizeRequests = reaAuthorizeRequests;
    }

    /**
     * 默认情况下,资源信息有权限中的任意一个权限标识,即可访问
     * @param paths
     * @param authors
     */
    @Override
    public boolean configurePathAuth(String[] paths, String... authors) {
        if(!this.checkSource(paths,authors)) {
            return false;
        }
        authorizeRequests.antMatchers(paths).hasAnyAuthority(authors);
        this.uriAuth2Cache(paths,authors);
        return true;
    }

    /**
     * 返回资源路径所需权限信息列表
     * @param path
     * @return
     */
    @Override
    public String[] getPathAuthors(String path) {
        if(DataCheckUtil.checkTextNullBool(path)) {
            return new String[0];
        }
        return uriAuthorMap.get(path);
    }

    /**
     * 资源与权限信息存入内存
     * @param paths
     * @param authors
     */
    protected void uriAuth2Cache(String[] paths, String... authors) {
        Arrays.stream(paths).forEach(uri -> AbstractWhiteUriAuthManager.uriAuthorMap.put(uri,authors));
    }

    /**
     * 基本数据校验
     * @param paths
     * @param authors
     * @return
     */
    protected boolean checkSource(String[] paths,String ... authors) {
        if(paths==null || paths.length < 1) {
            return false;
        }
        if(authors == null || authors.length < 1) {
            return false;
        }
        return true;
    }
}
