package cn.letterme.tools.shutdown.base.excutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(ShutdownFixedExcutor.class);
    
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
                LOGGER.debug("now setup a thread, threadIdx = " + threadIdx);
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
    
//    public static void main(String[] args) throws Exception
//    {
//        MachineModel machine1 = new MachineModel();
//        machine1.setIp("192.168.1.1");
//        machine1.setOsType(OsTypeEnum.Windows);
//        machine1.setPasswd("Changeme123");
//        machine1.setPasswd("administrator");
//        ShutdownServiceImpl service1 = ShutdownServiceImpl.newInstance(machine1);
//        Future<Object> future1 = ShutdownFixedExcutor.getInstance().submit(service1);
//        
//        MachineModel machine2 = new MachineModel();
//        machine2.setIp("10.10.10.10");
//        machine2.setOsType(OsTypeEnum.Windows);
//        machine2.setPasswd("Changeme123");
//        machine2.setPasswd("administrator");
//        ShutdownServiceImpl service2 = ShutdownServiceImpl.newInstance(machine2);
//        Future<Object> future2 = ShutdownFixedExcutor.getInstance().submit(service2);
//        
//        MachineModel machine3 = new MachineModel();
//        machine3.setIp("192.168.1.1");
//        machine3.setOsType(OsTypeEnum.Linux);
//        machine3.setPasswd("Changeme123");
//        machine3.setPasswd("administrator");
//        ShutdownServiceImpl service3 = ShutdownServiceImpl.newInstance(machine3);
//        Future<Object> future3 = ShutdownFixedExcutor.getInstance().submit(service3);
//        
//        while (!future1.isDone())
//        {
//            Thread.sleep(1000);
//        }
//        while (!future2.isDone())
//        {
//            Thread.sleep(1000);
//        }
//        while (!future3.isDone())
//        {
//            Thread.sleep(1000);
//        }
//        System.out.println(future1.get());
//        System.out.println(future2.get());
//        System.out.println(future3.get());
//    }
}
