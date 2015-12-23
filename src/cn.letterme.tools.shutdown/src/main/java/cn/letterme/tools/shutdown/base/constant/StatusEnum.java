package cn.letterme.tools.shutdown.base.constant;

/**
 * 状态枚举类
 * 
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public enum StatusEnum
{
    /**
     * 在线
     */
    Online(1, "Online"),
    
    /**
     * 离线
     */
    Offline(-1, "Offline"), 
    
    /**
     * 未知
     */
    Unknow(0, "Unknow");

    private int value;
    
    private String status;

    /**
     * 构造函数
     * @param value 值
     * @param status 状态
     */
    StatusEnum(int value, String status)
    {
        this.value = value;
        this.status = status;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public String getStatus()
    {
        return status;
    }
}
