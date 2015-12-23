package cn.letterme.tools.shutdown.base.excutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(PingScheduledExcutor.class);
    
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
        
        LOGGER.info("now setup the excutor, delay = " + delay + ", period = " + period + ", corePoolSize = " + corePoolSize);
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
    
//    public static void main(String[] args)
//    {
//        MachineModel machine1 = new MachineModel();
//        machine1.setIp("192.168.1.1");
//        MachineModel machine2 = new MachineModel();
//        machine2.setIp("10.10.10.10");
//        ICallback callback = new ICallback()
//        {
//            public MachineModel callback(MachineModel input)
//            {
//                System.out.println("callback " + input);
//                return null;
//            }
//        };
//        
//        PingServiceImpl service1 = new PingServiceImpl(machine1, 3);
//        service1.setCallback(callback);
//        PingServiceImpl service2 = new PingServiceImpl(machine2, 3);
//        service2.setCallback(callback);
//        PingScheduledExcutor.getInstance().submit(service1);
//        PingScheduledExcutor.getInstance().submit(service2);
//    }
}
