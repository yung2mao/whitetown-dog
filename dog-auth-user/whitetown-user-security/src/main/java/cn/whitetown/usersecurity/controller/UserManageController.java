package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.ao.RoleUserQuery;
import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authea.annotation.WhiteAuthAnnotation;
import cn.whitetown.authea.modo.WhiteControlType;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.authcommon.entity.ao.UserBasicQuery;
import cn.whitetown.authcommon.entity.dto.UserBasicInfoDto;
import cn.whitetown.updown.util.ExcelUtil;
import cn.whitetown.usersecurity.downentity.UserExcelTemplate;
import cn.whitetown.usersecurity.service.UserManageService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;

/**
 * 用户管理
 * @author GrainRain
 * @date 2020/06/17 20:38
 **/
@RestController
@RequestMapping("/user")
@Validated
@WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "auth_user")
public class UserManageController {

    @Autowired
    private UserManageService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private ExcelUtil excelUtil = ExcelUtil.getInstance();

    /**
     * 分页查询用户信息
     * @param userQuery
     * @return
     */
    @GetMapping("/page")
    public ResponseData<ResponsePage<UserBasicInfoDto>> queryUserList(@Valid UserBasicQuery userQuery){
        WhiteToolUtil.defaultPage(userQuery);
        ResponsePage<UserBasicInfoDto> result =  service.queryUserBasicList(userQuery);
        return ResponseData.ok(result);
    }

    /**
     * 获取角色ID对应的所有用户信息
     * @param roleUserQuery
     * @return
     */
    @GetMapping("/role_users")
    public  ResponseData<ResponsePage<UserBasicInfoDto>> queryUserListByRoleId(@Valid RoleUserQuery roleUserQuery){
        WhiteToolUtil.defaultPage(roleUserQuery);
        ResponsePage<UserBasicInfoDto> result = service.queryUserByRoleId(roleUserQuery);
        return ResponseData.ok(result);
    }

    /**
     * 结果集下载为excel
     * @param userQuery
     * @param response
     */
    @GetMapping("/downloads")
    public void download(@Valid UserBasicQuery userQuery, HttpServletResponse response) {
        int limitSize = DogBaseConstant.DOWN_FILE_MAX_ROW;
        userQuery.setPage(1);
        userQuery.setSize(limitSize);
        ResponsePage<UserBasicInfoDto> result =  service.queryUserBasicList(userQuery);
        String fileName = "user" + WhiteFormatUtil.dateFormat("yyyy-MM-dd",new Date());
        String sheetName = "sheet0";
        try {
            excelUtil.writeWebExcel(response,result.getResultList(),fileName,sheetName, UserExcelTemplate.class);
        } catch (IOException e) {
            throw new WhResException(ResponseStatusEnum.DOWN_FILE_ERROR);
        }
    }

    /**
     * 新增用户操作 - 只需填写用户基本的信息即可
     * 必须按参数 username password
     * 可选参数 roleName
     * @param params
     * @return
     */
    @PostMapping(value = "/registry",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "user_add_button")
    public ResponseData registryUser(@RequestBody JSONObject params){
        String username = params.getString("username");
        String password = params.getString("password");
        String roleName = params.getString("roleName");
        if(DataCheckUtil.checkTextNullBool(password)){
            password = AuthConstant.DEFAULT_PWD;
        }
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
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "auth_user_update")
    public ResponseData addUserBasicInfo(@RequestBody @Valid UserBasicInfo userInfo){
        service.updateUser(userInfo);
        return ResponseData.ok();
    }

    /**
     * 重置密码操作
     * 配置权限时只允许 - 超级管理员操作
     * @param username
     * @return
     */
    @GetMapping("/reset_pwd")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "user_reset_pwd")
    public ResponseData resetPassword(@NotBlank String username){
        service.resetPassword(username);
        return ResponseData.ok();
    }

    /**
     * 校验登录用户的原有密码
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "pwd_check",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation(type = WhiteControlType.AUTHENTICATED)
    public ResponseData<String> checkPassword(@RequestBody JSONObject jsonObject){
        String password = (String)jsonObject.get("password");
        if(DataCheckUtil.checkTextNullBool(password)){
            throw new WhResException(ResponseStatusEnum.OLD_PWD_NOT_RIGHT);
        }
        String username = jwtTokenUtil.getUsername();
        String pwdToken = service.checkPassword(username,password);
        return ResponseData.ok(pwdToken);
    }

    /**
     * 登录用户个人操作实现密码更新
     * 参数包括 pwdToken / newPassword
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "pwd_change",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation(type = WhiteControlType.AUTHENTICATED)
    public ResponseData updatePassword(@RequestBody JSONObject jsonObject){
        String pwdToken = jsonObject.getString("pwdToken");
        String newPassword = jsonObject.getString("newPassword");
        if(DataCheckUtil.checkTextNullBool(pwdToken) ||
            DataCheckUtil.checkTextNullBool(newPassword)){
            throw new WhResException(ResponseStatusEnum.ERROR_PARAMS);
        }
        String username = jwtTokenUtil.getUsername();
        service.updatePassword(username,pwdToken,newPassword);
        return ResponseData.ok();
    }

    /**
     * 用户状态变更
     * userStatus
     * 0 - 启用
     * 1 - 停用
     * 2 - 删除
     * @param username
     * @param userStatus
     * @return
     */
    @GetMapping("/status")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_ANY_AUTHORITY,value = {"auth_user_update","user_delete"})
    public ResponseData userActiveControl(@NotBlank String username,@NotNull @Min(0) @Max(2) Integer userStatus){
        if(AuthConstant.SUPER_MANAGE_USERNAME.equalsIgnoreCase(username)){
            return ResponseData.build(400,"禁止操作超级管理员状态",null);
        }
        service.changeUserStatus(username,userStatus);
        return ResponseData.ok();
    }
}
