package cn.letterme.tools.shutdown.base.excutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关机执行类
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class ShutdownFixedExcutor
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownFixedExcutor.class);
    
    /**
     * 线程池大小
     */
    private static final int THREAD_MAX_COUNT = 100;
    
    /**
     * 单例
     */
    private static final ShutdownFixedExcutor INSTANCE = new ShutdownFixedExcutor();
    
    /**
     * 当前最大线程序号
     */
    private int threadIdx = 0;
    
    /**
     * 线程数量
     */
    private int threadNum;
    
    /**
     * 线程执行服务
     */
    private ExecutorService excutorService;
    
    /**
     * 获取单例
     * @return 单例
     */
    public static ShutdownFixedExcutor getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * 构造函数
     * @param threadNum 线程数量
     */
    public ShutdownFixedExcutor(int threadNum)
    {
        this.threadNum = threadNum;
        excutorService = Executors.newFixedThreadPool(this.threadNum, new ThreadFactory()
        {
            public Thread newThread(Runnable r)
            {
                LOGGER.debug("now setup a thread, threadIdx : {}.", threadIdx);
                return new Thread(r, "Shutdown-Fixed-" + threadIdx++);
            }
        });
        LOGGER.info("now setup the excutor, threadNum = " + threadNum);
    }
   
    /**
     * 构造函数
     */
    public ShutdownFixedExcutor()
    {
        this(THREAD_MAX_COUNT);
    }

    /**
     * 提交callable对象
     * @param c callable对象
     * @return 执行结果
     */
    public <V> Future<V> submit(Callable<V> c)
    {
        LOGGER.debug("now submit a callable object to the excutor.");
        return excutorService.submit(c);
    }
    
    /**
     * 停止服务
     */
    public void terminate()
    {
        LOGGER.info("now shutdown the excutor.");
        excutorService.shutdownNow();
    }
}
