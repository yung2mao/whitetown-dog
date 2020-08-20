package cn.whitetown.updown.manager.wiml;

import cn.hutool.core.thread.ThreadUtil;
import cn.whitetown.dogbase.thread.WhSimpleThreadPoolFactory;
import cn.whitetown.updown.manager.ExcelReadManager;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author taixian
 * @date 2020/08/20
 *
 * !!每次读取excel都要new
 **/
public class AsyncExcelListener<T> extends AnalysisEventListener<T> {

    private ExcelReadManager<T> readManager;

    private ExecutorService executorService;

    private AsyncExcelListener() {
        int defaultCore = 4;
        String defaultThreadName = "excelRead";
        executorService = new ThreadPoolExecutor(defaultCore,
                defaultCore,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new WhSimpleThreadPoolFactory(defaultThreadName));
    }

    public AsyncExcelListener(ExcelReadManager<T> readManager) {
        this();
        this.readManager = readManager;
    }

    public AsyncExcelListener(ExcelReadManager<T> readManager, ExecutorService executorService) {
        this.readManager = readManager;
        this.executorService = executorService;
    }

    public AsyncExcelListener(ExcelReadManager<T> readManager, ExecutorService executorService, Semaphore semaphore) {
        this.readManager = readManager;
        this.executorService = executorService;
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        executorService.submit(()->readManager.readRow(t));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        readManager.doAfterAllAnalysed();
        List<T> data = readManager.getData();
        System.out.println(data);
        try {
            executorService.shutdown();
        }finally {
            if(!executorService.isShutdown()) {
                ThreadUtil.sleep(5000);
                executorService.shutdownNow();
            }
        }
    }
}
