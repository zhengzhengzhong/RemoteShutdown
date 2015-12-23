package cn.letterme.tools.shutdown.base.service;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import cn.letterme.tools.shutdown.base.ICallback;
import cn.letterme.tools.shutdown.base.model.TaskResult;

/**
 * 抽象服务类
 * 用于执行关机服务、ping服务
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public abstract class AbstractService implements Callable<Object>
{
    /**
     * 日志
     */
    private static final Logger LOGGER = Logger.getLogger(AbstractService.class);
    
    /**
     * 回调
     */
    protected ICallback callback;

    /**
     * 预处理
     * @throws Exception 异常
     */
    public abstract void before() throws Exception;

    /**
     * 进行处理
     * @return 返回结果
     * @throws Exception 异常
     */
    public abstract TaskResult excute() throws Exception;

    /**
     * 执行处理
     * 先预处理，预处理成功执行操作，再执行成功后，如果有回调则执行回调
     */
    public TaskResult call()
    {
        TaskResult obj = null;
        try
        {
            before();
            obj = excute();
            if (callback != null)
            {
                LOGGER.debug("excute the callback.");
                callback.callback(obj.getMachineModel());
            }
            else
            {
                LOGGER.info("the callback is null, needn't to excute it.");
            }
        }
        catch (Exception e)
        {
            LOGGER.error("excute the task error.", e);
            return TaskResult.newInstance(false, e.getMessage());
        }
        return TaskResult.newInstance(true, "", obj.getMachineModel());
    }

    public ICallback getCallback()
    {
        return callback;
    }

    public void setCallback(ICallback callback)
    {
        this.callback = callback;
    }
}
