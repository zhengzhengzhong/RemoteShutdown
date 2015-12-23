package cn.letterme.tools.shutdown.base.constant;

/**
 * 操作系统类型枚举类
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public enum OsTypeEnum
{
    /**
     * Windows
     */
    Windows(1, "Windows"),
    
    /**
     * Linux
     */
    Linux(2, "Linux");
    
    private int value;
    private String osName;
    
    /**
     * 构造函数
     * @param value 值
     * @param osName 系统名称
     */
    OsTypeEnum(int value, String osName)
    {
        this.value = value;
        this.osName = osName;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public String getOsName()
    {
        return osName;
    }
}
