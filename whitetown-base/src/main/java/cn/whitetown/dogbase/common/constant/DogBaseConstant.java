package cn.whitetown.dogbase.common.constant;

import org.springframework.core.Ordered;

/**
 * 通用常量信息
 * @author taixian
 * @date 2020/07/11
 **/
public class DogBaseConstant {
    /*\***************基础信息********************\*/

    public static final long CURRENT_WORK_ID = 01;

    /*\***************通用状态码********************\*/

    public static final int ACTIVE_NORMAL = 0;
    public static final int DISABLE_WARN = 1;
    public static final int DELETE_ERROR = 2;
    /**
     * 初始版本号
     */
    public static final int INIT_VERSION = 0;

    /*\***********过滤器优先级***************\*/

    public static final int GLOBAL_FILTER_LEVEL = Ordered.HIGHEST_PRECEDENCE;
    public static final int LOG_FILTER_LEVEL = Ordered.HIGHEST_PRECEDENCE + 10;
    public static final int SOCKET_CONNECT_FILTER_LEVEL = Ordered.HIGHEST_PRECEDENCE+20;

    /*\*****************文件相关*********************\*/

    /**
     * 下载文件最大数据行
     */
    public static final int DOWN_FILE_MAX_ROW = 5000;
}
