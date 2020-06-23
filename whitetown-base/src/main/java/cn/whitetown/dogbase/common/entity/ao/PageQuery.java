package cn.whitetown.dogbase.common.entity.ao;

import cn.whitetown.dogbase.db.annotation.QueryField;
import cn.whitetown.dogbase.db.annotation.QueryTable;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 用于分页查询的基本条件信息
 * @author GrainRain
 * @date 2020/06/20 15:52
 **/
@Getter
@Setter
@QueryTable
public class PageQuery {
    /**
     * 页码
     */
    @QueryField(ignore = true)
    private Integer page;
    /**
     * 查询的条数
     */
    @QueryField(ignore = true)
    private Integer size;
    /**
     * 起始时间
     */
    @QueryField(ignore = true)
    private String startTime;

    /**
     * 结束时间
     */
    @QueryField(ignore = true)
    private String endTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
