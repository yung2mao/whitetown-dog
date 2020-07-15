package cn.whitetown.authcommon.entity.ao;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 基于角色ID查询用户数据
 * @author taixian
 * @date 2020/07/15
 **/
@Getter
@Setter
public class RoleUserQuery extends PageQuery {
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
