package cn.letterme.tools.shutdown.base.service;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;
import static net.sf.expectit.matcher.Matchers.contains;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import cn.letterme.tools.shutdown.base.model.MachineModel;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;

/**
 * Linux机器关机服务
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class ShutdownServiceLinuxImpl extends ShutdownServiceImpl
{
    /**
     * 日志
     */
    private static final Logger LOGGER = Logger.getLogger(ShutdownServiceLinuxImpl.class);
    
    /**
     * 构造函数
     * @param machine 待关机对象
     */
    public ShutdownServiceLinuxImpl(MachineModel machine)
    {
        super(machine);
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown()
    {
        LOGGER.info("excute the shutdown operation.");
        
        Channel channel = null;
        Expect expect = null;
        Session session = null;
        try
        {
            JSch jSch = new JSch();
            session = jSch.getSession(machine.getUser(), machine.getIp());
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(machine.getPasswd());
            session.connect();
            channel = session.openChannel("shell");
            channel.connect();

            expect = new ExpectBuilder().withOutput(channel.getOutputStream())
                    .withInputs(channel.getInputStream(), channel.getExtInputStream()).withEchoInput(System.out)
                    .withEchoOutput(System.err).withInputFilters(removeColors(), removeNonPrintable())
                    .withExceptionOnFailure().build();

            expect.sendLine("whoami");
            Result rs = expect.expect(contains("root"));
            System.out.println(rs.isSuccessful());
            expect.sendLine("shutdown");
            rs = expect.expect(contains("power-off"));
            System.out.println(rs.isSuccessful());
        }
        catch (JSchException e)
        {
            LOGGER.error("excute shutdown operation error, ip = " + machine.getIp() + ", ", e);
        }
        catch (IOException e)
        {
            LOGGER.error("excute shutdown operation error, ", e);
        }
        finally
        {
            try
            {
                expect.close();
            }
            catch (IOException e)
            {
                LOGGER.error("close expect error, ", e);
            }
            channel.disconnect();
            session.disconnect();
        }
    }
}
