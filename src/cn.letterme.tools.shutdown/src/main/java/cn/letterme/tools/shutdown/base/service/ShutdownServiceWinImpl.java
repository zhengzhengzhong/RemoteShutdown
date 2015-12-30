package cn.letterme.tools.shutdown.base.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.letterme.tools.shutdown.base.model.MachineModel;

/**
 * Windows远程关机实现类
 * 
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class ShutdownServiceWinImpl extends ShutdownServiceImpl
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownServiceWinImpl.class);
    
    /**
     * 构造函数
     * @param model 待关机对象
     */
    public ShutdownServiceWinImpl(MachineModel model)
    {
        super(model);
    }

    /**
     * 远程关机命令模板 wmic /node:192.168.1.10/user:administrator /password:123456
     * process call create "shutdown.exe -r -f -m"
     */
    private static final String SHUTDONW_CMD_TEMPLATE = "wmic /node:%s /user:%s /password:%s process call create \"shutdown.exe /r /f /m\"";

    public void shutdown()
    {
        LOGGER.info("Begin excute the shutdown operation, machine info = {}.", machine.toString());
        
        String cmd = String.format(SHUTDONW_CMD_TEMPLATE, machine.getIp(), machine.getUser(), machine.getPasswd());
        try
        {
            Process process = Runtime.getRuntime().exec(cmd);
            StreamDrainerThread streamDrainer = new StreamDrainerThread(process.getInputStream());
            StreamDrainerThread errorStream = new StreamDrainerThread(process.getErrorStream());
            new Thread(streamDrainer).start();
            new Thread(errorStream).start();
            process.getOutputStream().close();
            process.waitFor();
            System.out.println("Environment Parameter OATS_HOME is " + streamDrainer.getOutput());
            System.out.println("Error: " + errorStream.getOutput());
        }
        catch (Exception e)
        {
            LOGGER.error("excute the shutdown operation error, ip : {}.", machine.getIp());
            LOGGER.error("", e);
        }
        
        LOGGER.info("End shutdown operation.");
    }

    /**
     * 输出流
     * @author zhengzhengzhong@outlook.com
     * @since 1.0.0
     */
    class StreamDrainerThread implements Runnable
    {
        private InputStream ins;

        private StringBuffer output = new StringBuffer();

        /**
         * 构造函数
         * @param ins 输入流
         */
        public StreamDrainerThread(InputStream ins)
        {
            this.ins = ins;
        }

        /**
         * {@inheritDoc}
         */
        public void run()
        {
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "GBK"));
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    if (!line.trim().isEmpty())
                    {
                        output.append(line);
                    }
                    else
                    {
                        output.append(File.separator);
                    }
                }
            }
            catch (Exception e)
            {
                LOGGER.error("Read message from remote machine error, ", e);
            }
        }
        
        /**
         * 获取输出流
         * @return 输出
         */
        public String getOutput()
        {
            return output.toString();
        }
    }
}
