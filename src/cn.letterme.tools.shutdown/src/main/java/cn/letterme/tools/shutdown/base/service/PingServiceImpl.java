package cn.letterme.tools.shutdown.base.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.letterme.tools.shutdown.base.constant.StatusEnum;
import cn.letterme.tools.shutdown.base.model.MachineModel;
import cn.letterme.tools.shutdown.base.model.TaskResult;

/**
 * Ping操作实现类
 * 
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class PingServiceImpl extends AbstractService implements Runnable
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PingServiceImpl.class);
    
    /**
     * Ping操作的超时时间 - 3秒
     */
    private static final int PING_TIMEOUT = 3000;
    
    private MachineModel model;
    
    private int retry;
    
    /**
     * 构造函数
     * @param model 目标机器配置
     * @param retry 重试次数
     */
    public PingServiceImpl(MachineModel model, int retry)
    {
        this.model = model;
        this.retry = retry;
    }

    /**
     * {@inheritDoc}
     */
    public void ping()
    {
        LOGGER.debug("Now begin ping the machine, machine info is : {}.", model.toString());
        
        // retry超出期望范围，默认为1
        if (retry <= 0)
        {
            LOGGER.info("the retry is out of expected ({}), set the default value(1).", retry);
            retry = 1;
        }

        LOGGER.info("excute ping, ip : {}, retry :{}. ", model.getIp(), retry);
        while (retry > 0)
        {
            try
            {
                boolean status = InetAddress.getByName(model.getIp()).isReachable(PING_TIMEOUT);
                if (status)
                {
                    model.setStatus(StatusEnum.Online);
                    LOGGER.debug("End ping the machine, status is [Online].");
                    return ;
                }
            }
            catch (UnknownHostException e)
            {
                LOGGER.error("excute ping task error.", e);
            }
            catch (IOException e)
            {
                LOGGER.error("excute ping task error.", e);
            }
            
            retry--;
        }
        
        LOGGER.debug("End ping the machine, status is [Offline].");
        model.setStatus(StatusEnum.Offline);
        return ;
    }

    public MachineModel getModel()
    {
        return model;
    }

    public void setModel(MachineModel model)
    {
        this.model = model;
    }

    public int getRetry()
    {
        return retry;
    }

    public void setRetry(int retry)
    {
        this.retry = retry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void before() throws Exception
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResult excute() throws Exception
    {
        ping();
        return TaskResult.newInstance(true, null, model);
    }

    /**
     * 执行处理
     * 用于周期执行的Ping操作
     */
    public void run()
    {
        ping();
        if (callback != null)
        {
            try
            {
                callback.callback(model);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
