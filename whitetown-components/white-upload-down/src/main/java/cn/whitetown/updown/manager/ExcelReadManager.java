package cn.whitetown.updown.manager;

import java.util.List;

/**
 * 读取web上传excel处理
 * @author taixian
 * @date 2020/08/20
 **/
public interface ExcelReadManager<T> {

    /**
     * 批量数据处理
     * @param dataList
     */
    void dataDeal(List<T> dataList);

    /**
     * 获取读取的数据集
     * @return
     */
    List<T> getData();

    /**
     * 是否处理完成
     * @return
     */
    boolean isFinish();

    /**
     * 读取完成调用
     */
    void doAfterAllAnalysed();
}
