package cn.whitetown.authcommon.entity.ao;

import cn.whitetown.dogbase.db.annotation.QueryField;
import cn.whitetown.dogbase.db.annotation.QueryTable;
import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息的查询条件
 * @author GrainRain
 * @date 2020/06/20 15:48
 **/
@Setter
@Getter
@QueryTable
public class UserBasicQuery extends PageQuery{
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户真实姓名
     */
    private String realName;
    /**
     * 用户性别
     */
    private String gender;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 用户电话
     */
    private String telephone;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户状态
     */
    private Integer userStatus;
    /**
     * 详情信息，用作简单匹配
     */
    @QueryField(ignore = true)
    private String searchDetail;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
