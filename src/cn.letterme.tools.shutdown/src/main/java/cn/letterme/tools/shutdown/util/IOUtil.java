package cn.letterme.tools.shutdown.util;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IO 工具类
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public final class IOUtil
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);
    
    /**
     * 关闭流
     * @param c 待关闭的流
     */
    public static void close(Closeable c)
    {
        if (c == null)
        {
            LOGGER.debug("The closeable is null.");
            return;
        }
        
        try
        {
            c.close();
        }
        catch (IOException e)
        {
            LOGGER.error("Close stream error.", e);
        }
    }
}
