package cn.whitetown.updown.manager;

import java.util.List;

/**
 * 读取web上传excel处理
 * @author taixian
 * @date 2020/08/20
 **/
public interface ExcelReadManager<T> {

    /**
     * 读取一行
     * @param t
     */
    void readRow(T t);

    /**
     * 数据分析
     */
    void dataDeal();

    /**
     * 获取data数据
     * @return
     */
    List<T> getData();

    /**
     * 读取完成调用
     */
    void doAfterAllAnalysed();

    /**
     * 处理完成后清理数据
     */
    void clear();
}
