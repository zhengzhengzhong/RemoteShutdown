package cn.letterme.tools.shutdown.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO 工具类
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public final class IOUtil
{
    public static void close(Closeable c)
    {
        if (c == null)
        {
            return;
        }
        
        try
        {
            c.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
