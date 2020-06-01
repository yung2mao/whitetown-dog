package cn.whitetown.dogbase.domain.vo;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 分页结果信息
 * @author GrainRain
 * @date 2020/05/27 21:34
 */
public class PageResult {
    /**
     * 查询到的总条数
     */
    private Integer total;
    /**
     * 查询第几页
     */
    private Integer page;
    /**
     * 查询多少条
     */
    private Integer rows;
    //分页查询的list结果
    private List<?> resultList;

    public PageResult() {
    }

    public PageResult(Integer total, List<?> resultList) {
        this.total = total;
        this.resultList = resultList;
    }


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public List<?> getResultList() {
        return resultList;
    }

    public void setResultList(List<?> resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
