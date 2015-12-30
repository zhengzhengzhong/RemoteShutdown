package cn.letterme.tools.shutdown.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加解密工具类
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public final class EncryptUtil
{
    /**
     * 日志
     */
    private static Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);
    
    /**
     * 加密
     * @param content 明文
     * @return 密文
     */
    public static String encrypt(String content)
    {
        LOGGER.debug("Begin encrypt.");
        LOGGER.debug("After encrypt : {}.", content);
        return content;
    }
    
    /**
     * 解密
     * @param content 密文
     * @return 明文
     */
    public static String decrypt(String content)
    {
        LOGGER.debug("Begin encrypt : {}.", content);
        LOGGER.debug("After encrypt.");
        return content;
    }
}
