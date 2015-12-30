package cn.letterme.tools.shutdown.config;

import java.io.File;

/**
 * 配置常量类
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class ConfigureConstants
{
    /**
     * 工具目录
     */
    public static final String ROOT = "tools.root";
    
    /**
     * 机器配置信息文件路径
     */
    public static final String MACHINE_CFG_PATH = getCfgPath("machine.cfg");
    
    /**
     * 应用配置文件路径
     */
    public static final String APPLICATION_CFG_PATH = getCfgPath("application.cfg");
    
    /**
     * 获取工具根目录
     * @return 工具根目录
     */
    private static String getRoot()
    {
        return System.getProperty(ROOT);
    }
    
    /**
     * 获取配置文件路径
     * @param cfgName 配置文件名称
     * @return 配置文件路径
     */
    private static String getCfgPath(String cfgName)
    {
        return getRoot() + File.separator + "conf" + File.separator + cfgName;
    }
}
