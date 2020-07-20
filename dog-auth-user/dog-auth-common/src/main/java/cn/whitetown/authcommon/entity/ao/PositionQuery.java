package cn.whitetown.authcommon.entity.ao;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 职位信息查询参数
 * @author taixian
 * @date 2020/07/20
 **/
@Getter
@Setter
public class PositionQuery extends PageQuery {
    private Long positionId;
    private Long deptId;
    private String deptCode;
    private String deptName;
    private String positionCode;
    private String positionName;
    private Integer positionLevel;
    private String description;
    /**
     * 用于多条件查询字段
     */
    private String detail;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
