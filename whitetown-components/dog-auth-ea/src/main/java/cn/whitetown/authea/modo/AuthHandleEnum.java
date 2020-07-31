package cn.whitetown.authea.modo;

import cn.whitetown.authea.manager.WhiteUriAuthManager;
import cn.whitetown.authea.manager.define.*;

/**
 * 处理资源和权限的枚举类
 * 根据类型返回相应的管理器
 * @author taixian
 * @date 2020/07/24
 **/
public enum AuthHandleEnum {
    /**
     * 访问不受限
     */
    PERMIT_ALL(WhiteControlType.PERMIT_ALL,new PermitAllManager()),
    /**
     * 登录后可访问
     */
    AUTHENTICATED(WhiteControlType.AUTHENTICATED,new AuthenticatedManager()),
    /**
     * 任一权限标识可访问
     */
    HAS_ANY_AUTHORITY(WhiteControlType.HAS_ANY_AUTHORITY,new HasAnyAuthorityManager()),
    /**
     * 有指定权限标识可访问
     */
    HAS_AUTHORITY(WhiteControlType.HAS_AUTHORITY,new HasAuthorityManager()),
    /**
     * 任一角色标识可访问
     */
    HAS_ANY_ROLE(WhiteControlType.HAS_ANY_ROLE,new HasAnyRoleManager()),
    /**
     * 指定该角色可访问
     */
    HAS_ROLE(WhiteControlType.HAS_ROLE,new HasRoleManager()),
    /**
     * 指定IP可访问
     */
    HAS_IP_ADDRESS(WhiteControlType.HAS_IP_ADDRESS,new HasIpAddressManager()),
    /**
     * 禁止访问
     */
    NO_AUTH(WhiteControlType.NO_AUTH,new NoAuthManager());

    /**
     * 类别
     */
    private String name;
    /**
     * 指定管理类
     */
    private WhiteUriAuthManager authManager;

    AuthHandleEnum(String name, WhiteUriAuthManager authManager) {
        this.name = name;
        this.authManager = authManager;
    }

    public static AuthHandleEnum authHandle(String type) {
        return valueOf(type);
    }

    public static WhiteUriAuthManager authManager(String type) {
        return valueOf(type) == null ? null : valueOf(type).authManager;
    }
}
