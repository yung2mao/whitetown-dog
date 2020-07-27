package cn.whitetown.authea.manager;

/**
 * 资源权限管理接口
 * @author taixian
 * @date 2020/07/24
 **/
public interface WhiteUriAuthManager {
    /**
     * 添加路径和权限信息到security配置中
     * @param paths
     * @param authors
     * @return
     */
    public boolean configurePathAuth(String[] paths, String ... authors);

    /**
     * 获取访问资源路径所需的权限信息
     * @param path
     * @return
     */
    String[] getPathAuthors(String path);
}
