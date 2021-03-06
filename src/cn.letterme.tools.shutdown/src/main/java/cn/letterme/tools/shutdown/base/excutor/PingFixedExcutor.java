package cn.letterme.tools.shutdown.base.excutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多线程执行Ping操作，立即执行，用于关机服务前置处理
 * 
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class PingFixedExcutor
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PingFixedExcutor.class);
    
    /**
     * 最大线程数量
     */
    private static final int THREAD_MAX_COUNT = 100;

    /**
     * 单例
     */
    private static final PingFixedExcutor INSTANCE = new PingFixedExcutor();

    /**
     * 最大线程序号
     */
    private int threadIdx = 0;

    /**
     * 当前线程池线程数量
     */
    private int threadNum;

    /**
     * 线程服务
     */
    private ExecutorService excutorService;

    /**
     * 获取单例
     * 
     * @return 单例
     */
    public static PingFixedExcutor getInstance()
    {
        return INSTANCE;
    }

    /**
     * 构造函数
     * 
     * @param threadNum
     *            线程数量
     */
    public PingFixedExcutor(int threadNum)
    {
        LOGGER.info("now setup the excutor, threadNum : {}.", threadNum);
        this.threadNum = threadNum;
        excutorService = Executors.newFixedThreadPool(this.threadNum, new ThreadFactory()
        {
            /**
             * 线程封装器
             */
            public Thread newThread(Runnable r)
            {
                LOGGER.debug("now setup a new thread, threadidx : {}.", threadIdx);
                return new Thread(r, "Ping-Fixed-" + threadIdx++);
            }
        });
    }

    /**
     * 构造函数
     */
    public PingFixedExcutor()
    {
        this(THREAD_MAX_COUNT);
    }

    /**
     * 提交callable对象
     * 
     * @param c
     *            callable对象
     * @return 执行结果
     */
    public <V> Future<V> submit(Callable<V> c)
    {
        LOGGER.debug("now submit a callable to the excutor.");
        return excutorService.submit(c);
    }

    /**
     * 关闭线程服务
     */
    public void terminate()
    {
        LOGGER.info("now shutdown the excutor.");
        excutorService.shutdownNow();
    }
}
