package cn.letterme.tools.shutdown.base.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(ShutdownServiceWinImpl.class);
    
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
        LOGGER.info("now excute the shutdown operation.");
        
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
            LOGGER.error("excute the shutdown operation error, ip = " + machine.getIp() + ",", e);
        }
    }
    
//    public static void main(String[] args)
//    {
//        MachineModel machine = new MachineModel();
//        machine.setIp("127.0.0.1");
//        machine.setUser("why");
//        machine.setPasswd("1234");
//        ShutdownServiceWinImpl shutdownServiceWinImpl = new ShutdownServiceWinImpl(machine);
//        shutdownServiceWinImpl.shutdown();
//    }

    class StreamDrainerThread implements Runnable
    {
        private InputStream ins;

        private StringBuffer output = new StringBuffer();

        public StreamDrainerThread(InputStream ins)
        {
            this.ins = ins;
        }

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
        
        public String getOutput()
        {
            return output.toString();
        }
    }
}
