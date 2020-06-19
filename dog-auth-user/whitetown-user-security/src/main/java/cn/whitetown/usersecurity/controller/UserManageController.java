package cn.whitetown.usersecurity.controller;

import cn.whitetown.dogbase.domain.vo.ResponseData;
import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.exception.CustomException;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.dogbase.util.DataCheckUtil;
import cn.whitetown.dogbase.util.FormatUtil;
import cn.whitetown.usersecurity.service.UserManageService;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;

/**
 * 用户管理
 * @author GrainRain
 * @date 2020/06/17 20:38
 **/
@RestController
@RequestMapping("/user")
public class UserManageController {
    @Autowired
    private UserManageService service;
    /**
     * 新增用户操作 - 只需填写用户基本的信息即可
     * 必须按参数 username password
     * 可选参数 roleName
     * @param params
     * @return
     */
    @PostMapping(value = "/registry",produces = "application/json;charset=UTF-8")
    public ResponseData registryUser(@RequestBody JSONObject params){
        String username = params.getString("username");
        String password = params.getString("password");
        String roleName = params.getString("roleName");
        if(DataCheckUtil.checkTextNullBool(roleName)){
            roleName = AuthConstant.DEFAULT_ROLE;
        }
        service.addUserBasicInfo(username,password,roleName);
        return ResponseData.ok();
    }

    /**
     * 完善个人信息 - username/password/roleName在此处不可做变更
     * 可由管理员或本人操作
     * @param userInfo
     * @return
     */
    @PostMapping(value = "update",produces = "application/json;charset=UTF-8")
    public ResponseData addUserBasicInfo(@RequestBody @Valid UserBasicInfo userInfo){
        service.updateUser(userInfo);
        return ResponseData.ok();
    }

    /**
     * 校验原有密码
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "pwdCheck",produces = "application/json;charset=UTF-8")
    public ResponseData<String> checkPassword(@RequestBody JSONObject jsonObject){
        String username = (String)jsonObject.get("username");
        String password = (String)jsonObject.get("password");
        if(DataCheckUtil.checkTextNullBool(username) || DataCheckUtil.checkTextNullBool(password)){
            throw new CustomException(ResponseStatusEnum.OLD_PWD_NOT_RIGHT);
        }
        String pwdToken = service.checkPassword(username,password);
        return ResponseData.ok(pwdToken);
    }

    /**
     * 密码更新操作
     * 参数包括 username / oldPassword / newPassword
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "pwdChange",produces = "application/json;charset=UTF-8")
    public ResponseData updatePassword(@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String pwdToken = jsonObject.getString("pwdToken");
        String newPassword = jsonObject.getString("newPassword");
        if(DataCheckUtil.checkTextNullBool(username) || DataCheckUtil.checkTextNullBool(pwdToken) ||
            DataCheckUtil.checkTextNullBool(newPassword)){
            throw new CustomException(ResponseStatusEnum.ERROR_PARAMS);
        }
        service.updatePassword(username,pwdToken,newPassword);
        return ResponseData.ok();
    }
}
