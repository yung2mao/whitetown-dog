package cn.whitetown.dogbase.common.entity.enums;

/**
 * 响应状态枚举类
 * @author GrainRain
 * @date 2020/05/26 20:14
 */
public enum ResponseStatusEnum {
    /**
     * 通用类别
     */
    SUCCESS(200,"SUCCESS"),   //请求成功
    FAIL(500,"SERVICE ERROR"),    //失败，服务器异常
    ERROR_PARAMS(400,"PARAMS ERROR"), //参数错误

    /**
     * 字典
     */
    DICT_EXISTED(400, "字典已经存在"),
    ERROR_CREATE_DICT(500, "创建字典失败"),
    ERROR_WRAPPER_FIELD(500, "包装字典属性失败"),
    ERROR_CODE_EMPTY(500, "字典类型不能为空"),

    /**
     * 文件上传
     */
    FILE_READING_ERROR(400, "FILE_READING_ERROR!"),
    FILE_NOT_FOUND(400, "FILE_NOT_FOUND!"),
    UPLOAD_ERROR(500, "上传图片出错"),

    /**
     * 权限和数据问题
     */
    DB_RESOURCE_NULL(400, "数据库中没有该资源"),
    NO_PERMITION(405, "权限异常"),
    REQUEST_INVALIDATE(400, "请求数据格式不正确"),
    INVALID_KAPTCHA(400, "验证码不正确"),
    CANT_DELETE_ADMIN(600, "不能删除超级管理员"),
    CANT_FREEZE_ADMIN(600, "不能冻结超级管理员"),
    CANT_CHANGE_ADMIN(600, "不能修改超级管理员角色"),

    /**
     * 账户问题
     */
    NOT_LOGIN(401, "当前用户未登录"),
    USER_ALREADY_REG(401, "该用户已经注册"),
    NO_THIS_USER(400, "没有此用户"),
    USER_NOT_EXISTED(400, "没有此用户"),
    ACCOUNT_FREEZED(401, "账号被冻结"),
    OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
    TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),
    NO_THIS_ROLE(400,"当前角色不存在"),
    CHECK_EXPIRE(402,"验证超时"),

    /**
     * 错误的请求
     */
    MENU_PCODE_COINCIDENCE(400, "菜单编号和副编号不能一致"),
    EXISTED_THE_MENU(400, "菜单编号重复，不能添加"),
    DICT_MUST_BE_NUMBER(400, "字典的值必须为数字"),
    REQUEST_NULL(400, "请求有错误"),
    SESSION_TIMEOUT(400, "会话超时"),
    SERVER_ERROR(500, "服务器异常"),

    /**
     * token异常
     */
    TOKEN_EXPIRED(700, "token过期"),
    TOKEN_ERROR(700, "token验证失败"),

    /**
     * 签名异常
     */
    SIGN_ERROR(700, "签名验证失败"),

    /**
     * 系统常量
     */
    ALREADY_CONSTANTS_ERROR(400, "已经存在该编码的系统参数"),
    SYSTEM_CONSTANT_ERROR(400, "不能删除系统常量"),

    /**
     * 登录相关
     */
    AUTH_REQUEST_ERROR(400, "账号密码错误"),
    AUTH_CAPTCHA_ERROR(400,"验证码错误"),
    AUTH_CAPTCHA_EXPIRE(400,"验证码已过期"),

    /**
     * ueditor相关异常
     */
    UE_CONFIG_ERROR(800, "读取ueditor配置失败"),
    UE_FILE_NULL_ERROR(801, "上传文件为空"),
    UE_FILE_READ_ERROR(803, "读取文件错误"),
    UE_FILE_SAVE_ERROR(802, "保存ue的上传文件出错"),

    /**
     * 菜单相关
     */
    MENU_LEVEL_ERROR(400,"菜单层级错误"),

    /**
     * 工作流相关
     */
    ACT_NO_FLOW(900, "无可用流程，请先导入或新建流程"),
    ACT_ADD_ERROR(901, "新建流程错误");

    /**
     * 状态码
     */
    private Integer status;
    /**
     * 状态解释
     */
    private String statusName;

    ResponseStatusEnum(Integer status, String typeName) {
        this.status = status;
        this.statusName = typeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
