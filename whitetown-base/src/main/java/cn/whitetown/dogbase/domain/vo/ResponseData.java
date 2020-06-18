package cn.whitetown.dogbase.domain.vo;

import com.alibaba.fastjson.JSON;

/**
 * 响应结果消息
 * @author GrainRain
 * @date 2020/05/27 20:34
 */
public class ResponseData<T> {
    /**
     * 状态码
     */
    private Integer status;
    /**
     * 携带附加信息
     */
    private String statusName;
    /**
     * 携带数据
     */
    private T data;

    /**
     * 自定义返回数据构造方法
     * @param status
     * @param statusName
     * @param data
     */
    private ResponseData(Integer status, String statusName, T data){
        this.status=status;
        this.statusName = statusName;
        this.data=data;
    }

    private ResponseData(ResponseStatusEnum responseStatusEnum, T data) {
        this.status = responseStatusEnum.getStatus();
        this.statusName = responseStatusEnum.getStatusName();
        this.data = data;
    }

    /**
     * 获取响应消息的方法
     * @param status 状态码
     * @param msg 附加信息
     * @param data 响应数据
     * @param <T> 类型
     * @return
     */
    public static <T> ResponseData<T> build(Integer status, String msg, T data){
        return new ResponseData<>(status, msg, data);
    }

    /**
     * 默认获取成功时的响应消息
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseData<T> ok(T data){
        return new ResponseData<>(ResponseStatusEnum.SUCCESS, data);
    }

    /**
     * 不携带数据的成功时响应
     * @return
     */
    public static ResponseData ok(){
        return new ResponseData(ResponseStatusEnum.SUCCESS,null);
    }

    /**
     * 返回失败的响应消息
     * @param responseStatusEnum 错误类别枚举
     * @return
     */
    public static ResponseData fail(ResponseStatusEnum responseStatusEnum){
        return new ResponseData(responseStatusEnum,null);
    }

    /**
     * 获取响应消息
     * @param responseStatusEnum
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseData<T> getInstance(ResponseStatusEnum responseStatusEnum, T data){
        return new ResponseData<>(responseStatusEnum,data);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
