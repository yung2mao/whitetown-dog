package cn.whitetown.event.modo;

/**
 * 队列元数据
 * @Author: taixian
 * @Date: created in 2020/10/26
 */
public class QueueMeta {

    /**
     * 组ID
     */
    private String groupId;

    /**
     * 名称
     */
    private String name;

    /**
     * 最大允许长度
     */
    private Integer maxSize;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 销毁时间
     */
    private Long invalidateTime;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getInvalidateTime() {
        return invalidateTime;
    }

    public void setInvalidateTime(Long invalidateTime) {
        this.invalidateTime = invalidateTime;
    }
}
