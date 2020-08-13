package cn.whitetown.dogbase.common.util;

/**
 * Description 基于雪花算法的ID生成实现
 *
 * @author GrainRain
 * @date 2020/06/02
 **/
public class SnowIDCreateUtil {

    /**
     * 机器码
     */
    private long workId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上次的时间戳，用于更新sequnce归零时判定
     */
    private long preTime = 0L;

    private static long timeOffset = 22L;
    private static long workIdOffset = 12L;

    /**
     * workId最大允许值
     */
    private static long maxWorkId = -1 ^ (-1 << 10L);

    /**
     * 最大序列 - 1毫秒允许4096个id生成
     */
    private static long maxSequence = -1 ^ (-1 << 12L);

    public SnowIDCreateUtil(long workId){
        if(workId < 0 || workId > maxWorkId){
            throw new IndexOutOfBoundsException("max work id:"+workId+", the real work id:"+workId);
        }
        this.workId = workId;
    }

    /**
     * 获取id值
     * @return
     */
    public long getSnowId(){
        long currentTime = System.currentTimeMillis();
        synchronized (SnowIDCreateUtil.class){
            sequence++;
            if(sequence > maxSequence) {
                while (currentTime <= preTime) {
                    try {
                        Thread.sleep(1);
                        currentTime = System.currentTimeMillis();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sequence = 0L;
            }
            preTime = currentTime;

            long id = (currentTime << timeOffset) | (workId << workIdOffset) | sequence;
            return id;
        }
    }
}
