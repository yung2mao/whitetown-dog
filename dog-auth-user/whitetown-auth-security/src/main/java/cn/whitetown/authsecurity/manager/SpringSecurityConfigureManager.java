package cn.whitetown.authsecurity.manager;

/**
 * @author taixian
 * @date 2020/07/24
 **/
public interface SpringSecurityConfigureManager {

    /**
     * 初始化方法
     */
    void init();

    /**
     * 执行资源和权限配置方法
     */
    public void authHandle();

    /**
     * 获取path所需权限信息
     * @param path
     * @return
     */
    String[] getAuthorsByPath(String path);

    /**
     * 任务结束方法
     */
    public void taskOver();
}
