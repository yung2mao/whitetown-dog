package cn.whitetown.authcommon.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户基础信息表示层
 * @author GrainRain
 * @date 2020/06/20 16:04
 **/
@Setter
@Getter
public class UserBasicInfoVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date birthday;
    /**
     * 性别
     */
    private String gender;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话号码
     */
    private String telephone;
    /**
     * 用户状态
     * 0 - 正常
     * 1 - 停用
     * 2 - 删除
     */
    private Integer userStatus;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
