package cn.letterme.tools.shutdown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.letterme.tools.shutdown.base.MachineDataHolder;

/**
 * 应用入口
 * 
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class Application
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    
    /**
     * 主函数
     * 
     * @param args
     *            入参
     */
    public static void main(String[] args)
    {
        // 初始化，注册回调
        init();
        
        // 加载数据
        MachineDataHolder.getInstance().loadMachineData();
    }

    /**
     * 初始化函数<br/>
     * 注册退出的函数，用来注册函数，在jvm退出前保存机器信息
     */
    private static void init()
    {
        // 注册一个关机钩，当系统被退出或被异常中断时，启动这个关机钩线程
        LOGGER.debug("Register the shutdown hook to save machine data.");
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                MachineDataHolder.getInstance().saveMachineData();
            }
        });
    }
}
