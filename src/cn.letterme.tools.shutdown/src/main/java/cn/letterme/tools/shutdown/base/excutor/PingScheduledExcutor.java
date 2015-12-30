package cn.letterme.tools.shutdown.base.excutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定期执行的Ping操作
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class PingScheduledExcutor
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PingScheduledExcutor.class);
    
    /**
     * 线程大小
     */
    private static final int CORE_POOL_SIZE = 100;
    
    /**
     * 单例
     * 0延迟，60s为周期执行
     */
    private static final PingScheduledExcutor INSTANCE = new PingScheduledExcutor(0, 60);
    
    /**
     * 线程数量
     */
    private int threadIdx = 0;

    /**
     * 延迟执行的时间，单位：s
     */
    private int delay;
    
    /**
     * 执行周期，单位：s
     */
    private int period;
    
    /**
     * 当前线程大小
     */
    private int corePoolSize;
    
    /**
     * 执行服务
     */
    private ScheduledExecutorService excutorService;
    
    /**
     * 获取单例
     * @return 单例
     */
    public static PingScheduledExcutor getInstance()
    {
        return INSTANCE;
    }

    /**
     * 构造函数
     * @param delay 延迟时间，单位：s
     * @param period 执行周期，单位：s
     */
    public PingScheduledExcutor(int delay, int period)
    {
        this(delay, period, CORE_POOL_SIZE);
    }
    
    /**
     * 构造函数
     * @param delay 延迟时间，单位：s
     * @param period 执行周期，单位：s
     * @param corePoolSize 线程数量
     */
    public PingScheduledExcutor(int delay, int period, int corePoolSize)
    {
        this.delay = delay;
        this.period = period;
        this.corePoolSize = corePoolSize;
        
        LOGGER.info("now setup the excutor, delay = {}, period = {}, corePoolSize = {}.", delay, period, corePoolSize);
        excutorService = Executors.newScheduledThreadPool(this.corePoolSize, new ThreadFactory()
        {
            public Thread newThread(Runnable r)
            {
                LOGGER.debug("now setup the thread, the threadIdx = " + threadIdx);
                return new Thread(r, "Ping-Scheduled-" + threadIdx++);
            }
        });
    }

    /**
     * 提交执行的服务
     * @param r 待执行的任务
     */
    public void submit(Runnable r)
    {
        LOGGER.debug("now submit a runnable object to the excutor.");
        excutorService.scheduleAtFixedRate(r, delay, period, TimeUnit.SECONDS);
    }
    
    /**
     * 停止
     */
    public void terminate()
    {
        LOGGER.info("now shutdown the excutor.");
        excutorService.shutdownNow();
    }
}
