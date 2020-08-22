package cn.whitetown.updown.manager.wiml;

import cn.hutool.core.thread.ThreadUtil;
import cn.whitetown.dogbase.common.util.WriteSyncList;
import cn.whitetown.dogbase.thread.WhSimpleThreadPoolFactory;
import cn.whitetown.updown.manager.ExcelReadManager;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 异步处理监听器
 * @author taixian
 * @date 2020/08/20
 *
 * !!每次读取excel都要new
 **/
public class AsyncExcelListener<T> extends AnalysisEventListener<T> {

    private List<T> bufferList;

    private int blockSize;

    private ExcelReadManager<T> readManager;

    private ExecutorService executorService;

    private Semaphore semaphore;

    public AsyncExcelListener(ExcelReadManager<T> readManager,int permitThread,int bufferSize) {
        bufferList = new WriteSyncList<>(new LinkedList<>());
        semaphore = new Semaphore(permitThread);
        String defaultThreadName = "excelRead";
        executorService = new ThreadPoolExecutor(permitThread,
                permitThread,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new WhSimpleThreadPoolFactory(defaultThreadName));
        this.readManager = readManager;
        this.blockSize = bufferSize / permitThread;
    }

    public AsyncExcelListener(ExcelReadManager<T> readManager) {
        this(readManager, 4,4000);
    }

    public AsyncExcelListener(ExcelReadManager<T> readManager,int bufferSize) {
        this(readManager,4, bufferSize);
    }

    @SneakyThrows
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        semaphore.acquire();
        if(bufferList.size() < blockSize) {
            bufferList.add(t);
            semaphore.release();
            return;
        }
        executorService.submit(() -> {
            synchronized (this) {
                if(bufferList.size() < blockSize) {
                    bufferList.add(t);
                    semaphore.release();
                    return;
                }
            }
            List<T> list = new LinkedList<>(bufferList);
            bufferList.clear();
            readManager.dataDeal(list);
            bufferList.add(t);
            semaphore.release();
        });
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        readManager.dataDeal(this.bufferList);
        readManager.doAfterAllAnalysed();
        try {
            executorService.shutdown();
        }finally {
            if(!executorService.isShutdown()) {
                ThreadUtil.sleep(5,TimeUnit.SECONDS);
                executorService.shutdownNow();
            }
        }
    }
}
