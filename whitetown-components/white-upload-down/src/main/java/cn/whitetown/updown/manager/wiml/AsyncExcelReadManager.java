package cn.whitetown.updown.manager.wiml;

import cn.whitetown.updown.manager.ExcelReadManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author taixian
 * @date 2020/08/20
 **/
public class AsyncExcelReadManager implements ExcelReadManager<Map<Integer,Object>> {

    private Log logger = LogFactory.getLog(AsyncExcelReadManager.class);

    private AtomicInteger blockSize = new AtomicInteger(2);

    private int blockCapacity = 2;

    private List<Map<Integer,Object>> dataList;

    private BlockingQueue<List<Map<Integer,Object>>> dataQueue;

    private AsyncExcelReadManager() {
        this(5);
    }

    private AsyncExcelReadManager(int blockSize) {
        dataList = new LinkedList<>();
        dataQueue = new LinkedBlockingDeque<>(blockSize);
    }

    private AsyncExcelReadManager(int blockCapacity, int blockSize) {
        this(blockSize);
        this.blockSize.set(blockCapacity);
        this.blockCapacity = blockCapacity;
    }

    public static ExcelReadManager<Map<Integer,Object>> getInstance() {
        return new AsyncExcelReadManager();
    }

    public static ExcelReadManager<Map<Integer,Object>> getInstance(int blockCapacity,int blockSize) {
        return new AsyncExcelReadManager(blockCapacity,blockSize);
    }

    @Override
    public void readRow(Map<Integer, Object> rowMap) {
        try {
            int base = blockSize.decrementAndGet();
            if(base < 1) {
                synchronized (this) {
                    if(dataList.size() > 1 && blockSize.get() < 1) {
                        dataQueue.offer(dataList,5, TimeUnit.SECONDS);
                        dataList = new LinkedList<>();
                        blockSize.set(this.blockCapacity);
                        base = this.blockCapacity;
                    }
                    blockSize.compareAndSet(base,blockSize.get()-1);
                    dataList.add(rowMap);
                }
            }else {
                dataList.add(rowMap);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void dataDeal() {
        List<Map<Integer, Object>> data = this.getData();
        System.out.println(data);
    }

    @Override
    public List<Map<Integer, Object>> getData() {
        return dataList;
    }

    @Override
    public void doAfterAllAnalysed() {
        logger.debug("read sheet finished");
    }

    @Override
    public void clear() {
        dataList.clear();
    }
}
