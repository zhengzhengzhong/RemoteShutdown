package cn.letterme.tools.shutdown.base.model;

/**
 * 任务执行结果
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class TaskResult
{
    /**
     * 详细信息
     */
    private String detail;
    
    /**
     * 结果，true-成功，false-失败
     */
    private boolean result;
    
    /**
     * 任务模型
     */
    private MachineModel machineModel;
   
    /**
     * 创建结果
     * @param result 是否成功
     * @param detail 详情
     * @return 结果
     */
    public static TaskResult newInstance(boolean result, String detail)
    {
        return newInstance(result, detail, null);
    }
    
    /**
     * 创建结果
     * @param result 是否成功
     * @param detail 详情
     * @param obj 机器模型
     * @return 结果
     */
    public static TaskResult newInstance(boolean result, String detail, MachineModel obj)
    {
        return new TaskResult(result, detail, obj);
    }
    
    /**
     * 构造函数
     * @param result 是否成功
     * @param detail 详情
     * @param machineModel 机器模型
     */
    private TaskResult(boolean result, String detail, MachineModel machineModel)
    {
        this.result = result;
        this.detail = detail;
        this.machineModel = machineModel;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public boolean isResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public MachineModel getMachineModel()
    {
        return machineModel;
    }

    public void setMachineModel(MachineModel machineModel)
    {
        this.machineModel = machineModel;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[Result = ").append(result);
        if (null != detail && !detail.trim().isEmpty())
        {
            sb.append(", detail = ").append(detail);
        }
        if (null != machineModel)
        {
            sb.append(", machineModel = ").append(machineModel.toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
