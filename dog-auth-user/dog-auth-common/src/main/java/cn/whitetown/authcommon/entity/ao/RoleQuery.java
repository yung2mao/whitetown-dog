package cn.whitetown.authcommon.entity.ao;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.annotation.QueryField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单查询
 * @author taixian
 * @date 2020/07/13
 **/
@Getter
@Setter
public class RoleQuery {
    @QueryField(operation = "like")
    private String roleName;

    @QueryField(operation = "like")
    private String description;

    @QueryField(ignore = true)
    private String detail;

    @QueryField(operation = "ge")
    @Setter(value= AccessLevel.NONE)
    private String startTime;

    @QueryField(operation = "le")
    @Setter(value=AccessLevel.NONE)
    private String endTime;

    public void setStartTime(String startTime) {
        this.startTime = DataCheckUtil.checkTextNullBool(startTime) ? null : startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = DataCheckUtil.checkTextNullBool(endTime) ? null : endTime;
    }
}
