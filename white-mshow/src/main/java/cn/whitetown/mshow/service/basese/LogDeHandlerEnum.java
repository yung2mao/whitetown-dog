package cn.whitetown.mshow.service.basese;

/**
 * @author taixian
 * @date 2020/09/16
 **/
public enum LogDeHandlerEnum {
    /**
     * 系统日志
     */
    SYS_LOG(new SysLogQueryHandler());

    private LogDetailHandler logDetailHandler;

    LogDeHandlerEnum(LogDetailHandler logDetailHandler) {
        this.logDetailHandler = logDetailHandler;
    }

    public static LogDetailHandler logDetailHandler(String name) {
        return valueOf(name).logDetailHandler;
    }
}
