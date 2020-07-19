package cn.whitetown.authcommon.entity.dto;

import cn.whitetown.authcommon.entity.AbstractUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 登录用户的基础信息
 * @author GrainRain
 * @date 2020/05/30 15:21
 **/
@Setter
@Getter
public class LoginUser extends AbstractUser {
    /**
     * 头像信息
     */
    private String avatar;
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
     * 所属部门ID
     */
    private Long deptId;
    /**
     * 所属部门名称
     */
    private String deptName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话号码
     */
    private String telephone;

    public LoginUser() {
    }

    public LoginUser(String avatar, String realName, Date birthday,
                     String gender, String email, String telephone) {
        this.avatar = avatar;
        this.realName = realName;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
