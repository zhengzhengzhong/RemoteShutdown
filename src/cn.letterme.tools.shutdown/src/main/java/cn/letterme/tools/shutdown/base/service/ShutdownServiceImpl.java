package cn.letterme.tools.shutdown.base.service;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import cn.letterme.tools.shutdown.base.constant.OsTypeEnum;
import cn.letterme.tools.shutdown.base.constant.StatusEnum;
import cn.letterme.tools.shutdown.base.excutor.PingFixedExcutor;
import cn.letterme.tools.shutdown.base.model.MachineModel;
import cn.letterme.tools.shutdown.base.model.TaskResult;

/**
 * 关机服务类
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public abstract class ShutdownServiceImpl extends AbstractService
{
    /**
     * 日志
     */
    private static final Logger LOGGER = Logger.getLogger(ShutdownServiceImpl.class);
    
    /**
     * 待关机对象
     */
    protected MachineModel machine;
    
    /**
     * 构造函数
     * @param machine 待关机对象
     */
    public ShutdownServiceImpl(MachineModel machine)
    {
        this.machine = machine;
    }
    
    /**
     * 获取服务对象
     * @param machine 待关机对象
     * @return 服务对象
     */
    public static ShutdownServiceImpl newInstance(MachineModel machine)
    {
        if (null == machine)
        {
            LOGGER.error("the machine is null.");
            return null;
        }
        
        if (OsTypeEnum.Windows.equals(machine.getOsType()))
        {
            LOGGER.info("the osType is windows, return the ShutdownServiceWinImpl.");
            return new ShutdownServiceWinImpl(machine);
        }
        
        if (OsTypeEnum.Linux.equals(machine.getOsType()))
        {
            LOGGER.info("the osType is linux, return the ShutdownServiceLinuxImpl.");
            return new ShutdownServiceLinuxImpl(machine);
        }
        
        LOGGER.error("unknown osType (" + machine.getOsType() + "), will return null.");
        return null;
    }
    
    /**
     * 关机操作
     * @throws Exception 异常
     */
    public abstract void shutdown() throws Exception;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void before() throws Exception
    {
        LOGGER.info("excute the before operation.");
        
        // 通过ping看机器是否在线，重试3次
        Future<Object> future = PingFixedExcutor.getInstance().submit(new PingServiceImpl(machine, 3));
        
        // 等待执行是否完成，超时30s
        Object returnObj = future.get(30, TimeUnit.SECONDS);
        this.machine = ((TaskResult) returnObj).getMachineModel();
        if (machine == null)
        {
            LOGGER.error("the machine is null.");
            throw new Exception("Machine is null");
        }
        
        if (machine.getStatus() != StatusEnum.Online)
        {
            LOGGER.error("the machine is offline, ip = " + machine.getIp());
            throw new Exception("Machine status is not online, status = " + machine.getStatus() + ", ip = " + machine.getIp());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResult excute() throws Exception
    {
        before();
        shutdown();
        return TaskResult.newInstance(true, null, machine);
    }
}
