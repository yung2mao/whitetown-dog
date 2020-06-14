package cn.whitetown.dogbase.user.entity;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.List;

/**
 * 登录用户的基础信息
 * @author GrainRain
 * @date 2020/05/30 15:21
 **/
public class LoginUser extends AbstractUser{
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

    public LoginUser() {
    }

    public LoginUser(String avatar, String salt, String realName, Date birthday,
                     String gender, String email, String telephone) {
        this.avatar = avatar;
        this.salt = salt;
        this.realName = realName;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.telephone = telephone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
