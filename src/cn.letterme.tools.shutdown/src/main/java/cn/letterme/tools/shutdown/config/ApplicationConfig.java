package cn.letterme.tools.shutdown.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.letterme.tools.shutdown.base.model.MachineModel;
import cn.letterme.tools.shutdown.util.IOUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 配置类<br/>
 * 用于保存程序配置信息<br/>
 * 比如：机器信息<br/>
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class ApplicationConfig
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);
    
    /**
     * 单例
     */
    private static final ApplicationConfig INSTANCE = new ApplicationConfig();
    
    /**
     * 获取单例
     * @return 单例
     */
    public static final ApplicationConfig getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * 加载机器信息
     * @return 机器信息
     */
    public List<MachineModel> loadMachines()
    {
        LOGGER.debug("Begin load machines");
        List<MachineModel> machines = new ArrayList<MachineModel>();
        
        String machineInfoCfg = ConfigureConstants.MACHINE_CFG_PATH;
        File cfgFile = new File(machineInfoCfg);
        
        if (!cfgFile.exists())
        {
            LOGGER.error("machine.cfg not exist.");
            return machines;
        }
        
        FileReader fr = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            fr = new FileReader(cfgFile);
            
            while (fr.ready())
            {
                sb.append((char)fr.read());
            }
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("read the machine infos error, ", e);
        }
        catch (IOException e)
        {
            LOGGER.error("read the machine infos error, ", e);
        }
        finally
        {
            IOUtil.close(fr);
        }
        
        String machinesInfo = sb.toString();
        if (machinesInfo.trim().isEmpty())
        {
            LOGGER.debug("The machine info is null or empty.");
            return machines;
        }
        
        JSONArray jsonArray = JSONArray.fromObject(machinesInfo);
        
        int size = jsonArray.size();
        for (int idx = 0; idx < size; idx++)
        {
            JSONObject jsonObj = jsonArray.getJSONObject(idx);
            MachineModel machine = MachineModel.fromJSONObject(jsonObj);
            if (null != machine)
            {
                LOGGER.debug("The machine info is : ip = {}, osType = {}.", machine.getIp(), machine.getOsType());
                machines.add(machine);
            }
        }
        
        return machines;
    }
    
    /**
     * 保存机器信息
     * @param machines 机器信息
     */
    public void saveMachineModel(List<MachineModel> machines)
    {
        LOGGER.debug("Begin save machine infos.");
        if (null == machines || machines.isEmpty())
        {
            LOGGER.error("save the machine infos error, due to the machines is null or empty.");
            return;
        }
        
        String machineInfoCfg = ConfigureConstants.MACHINE_CFG_PATH;
        File cfgFile = new File(machineInfoCfg);
        
        JSONArray jsonArray = new JSONArray();
        for (MachineModel machine : machines)
        {
            if (null == machine)
            {
                continue;
            }
            LOGGER.debug("The machine info is: ip = {}, osType = {}.", machine.getIp(), machine.getOsType());
            jsonArray.add(machine.toJsonObject());
        }
        
        FileWriter fw = null;
        try
        {
            if (!cfgFile.exists())
            {
                cfgFile.createNewFile();
            }
            
            fw = new FileWriter(cfgFile);
            fw.write(jsonArray.toString());
        }
        catch (IOException e)
        {
            LOGGER.error("save the machine infos error, ", e);
        }
        finally
        {
            IOUtil.close(fw);
        }
    }
}
