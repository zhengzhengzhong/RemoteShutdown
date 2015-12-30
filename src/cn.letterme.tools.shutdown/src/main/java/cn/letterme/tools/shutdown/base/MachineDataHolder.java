package cn.letterme.tools.shutdown.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.letterme.tools.shutdown.base.model.MachineModel;
import cn.letterme.tools.shutdown.config.ApplicationConfig;

/**
 * 机器数据缓存
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class MachineDataHolder
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MachineDataHolder.class);
    
    /**
     * 单例
     */
    private static final MachineDataHolder INSTANCE = new MachineDataHolder();
    
    /**
     * 机器数据
     */
    private Map<String, MachineModel> machineDataMap = new HashMap<String, MachineModel>();
    
    /**
     * 获取单例
     * @return 单例
     */
    public static MachineDataHolder getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * 加载机器数据
     */
    public void loadMachineData()
    {
        LOGGER.info("Load machine data...");
        List<MachineModel> machineList = ApplicationConfig.getInstance().loadMachines();
        if (CollectionUtils.isEmpty(machineList))
        {
            LOGGER.error("The machine data is null or empty");
            return;
        }
        for (MachineModel model : machineList)
        {
            if (null == model)
            {
                continue;
            }
            LOGGER.debug("The machine info is: ip = {}, osType = {}.", model.getIp(), model.getOsType());
            machineDataMap.put(model.getIp(), model);
        }
    }
    
    /**
     * 保存机器数据
     */
    public void saveMachineData()
    {
        LOGGER.info("Save machine data...");
        List<MachineModel> machineDataList = new ArrayList<MachineModel>();
        Set<Entry<String, MachineModel>> entrySet = machineDataMap.entrySet();
        
        Iterator<Entry<String, MachineModel>> it = entrySet.iterator();
        while (it.hasNext())
        {
            Entry<String, MachineModel> entry = it.next();
            MachineModel model = entry.getValue();
            if (model != null)
            {
                LOGGER.debug("The machine info is: ip = {}, osType = {}.", model.getIp(), model.getOsType());
                machineDataList.add(model);
            }
        }
        
        ApplicationConfig.getInstance().saveMachineModel(machineDataList);
    }
    
    /**
     * 获取机器数据列表
     * @return 机器数据
     */
    public List<MachineModel> getMachineDataList()
    {
        List<MachineModel> machineDataList = new ArrayList<MachineModel>();
        Set<Entry<String, MachineModel>> entrySet = machineDataMap.entrySet();
        
        Iterator<Entry<String, MachineModel>> it = entrySet.iterator();
        while (it.hasNext())
        {
            Entry<String, MachineModel> entry = it.next();
            MachineModel model = entry.getValue();
            if (model != null)
            {
                LOGGER.debug("The machine info is: ip = {}, osType = {}.", model.getIp(), model.getOsType());
                machineDataList.add(model);
            }
        }
        return machineDataList;
    }
    
    /**
     * 设置机器数据列表
     * @param machineDataList 机器数据
     */
    public void setMachineDataList(List<MachineModel> machineDataList)
    {
        if (CollectionUtils.isEmpty(machineDataList))
        {
            LOGGER.error("The machine data is null or empty");
            return;
        }
        for (MachineModel model : machineDataList)
        {
            if (null == model)
            {
                continue;
            }
            LOGGER.debug("The machine info is: ip = {}, osType = {}.", model.getIp(), model.getOsType());
            machineDataMap.put(model.getIp(), model);
        }
    }
    
    /**
     * 根据序号获取机器数据
     * @param ip 机器的IP地址
     * @return 机器数据
     */
    public MachineModel getMachineDataByIp(String ip)
    {
        if (StringUtils.isBlank(ip))
        {
            LOGGER.error("Get machine data by index error, ip : {}.", ip);
            return null;
        }
        LOGGER.debug("Get machine : ip = {}.", ip);
        return machineDataMap.get(ip);
    }
    
    /**
     * 更新机器数据
     * @param machineData 机器数据
     */
    public void updateMachineData(MachineModel machineData)
    {
        if (null == machineData)
        {
            LOGGER.error("The machine data is null.");
            return;
        }
        
        String ip = machineData.getIp();
        if (StringUtils.isBlank(ip))
        {
            LOGGER.error("The machine data is invalid, machine data : {}.", machineData.toString());
            return;
        }
        LOGGER.debug("Update machine Info : ip = {}, osType = {}.", machineData.getIp(), machineData.getOsType());
        machineDataMap.put(ip, machineData);
    }
}
