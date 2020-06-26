package cn.whitetown.authcommon.entity.po;

import cn.whitetown.authcommon.entity.AbstractUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 用户基本信息类
 * @author GrainRain
 * @Date 2020-05-01
 */
@Setter
@Getter
@TableName("user_basic_info")
public class UserBasicInfo extends AbstractUser {
    /**
     * 头像信息
     */
    private String avatar;
    /**
     * 密码盐
     */
    private String salt;
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
    @Email
    private String email;
    /**
     * 电话号码
     */
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$")
    private String telephone;
    /**
     * 用户状态
     */
    private Integer userStatus;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建人id
     */
    private Long createUserId;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新人id
     */
    private Long updateUserId;
    /**
     * 用户版本号
     */
    private Integer userVersion;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}