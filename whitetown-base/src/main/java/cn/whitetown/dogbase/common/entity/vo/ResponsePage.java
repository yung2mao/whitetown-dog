package cn.whitetown.dogbase.common.entity.vo;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页结果信息
 * @author GrainRain
 * @date 2020/05/27 21:34
 */
@Getter
@Setter
public class ResponsePage<T> {
    /**
     * 查询到的总条数
     */
    private Long total;
    /**
     * 查询第几页
     */
    private Long page;
    /**
     * 查询多少条
     */
    private Long rows;
    /**
     * 查询的结果信息
     */
    private List<T> resultList;

    public ResponsePage() {
    }

    public ResponsePage(Long page, Long rows, Long total, List<T> resultList) {
        this.page = page;
        this.rows = rows;
        this.total = total;
        this.resultList = resultList;
    }
    
    public static <T> ResponsePage<T> createPage(Long page, Long rows, Long total, List<T> resultList){
        return new ResponsePage<>(page,rows,total,resultList);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
