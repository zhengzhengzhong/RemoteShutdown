package cn.letterme.tools.shutdown.base.model;

import cn.letterme.tools.shutdown.base.constant.OsTypeEnum;
import cn.letterme.tools.shutdown.base.constant.StatusEnum;
import net.sf.json.JSONObject;

/**
 * 机器模型
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public class MachineModel
{
    /**
     * IP地址
     */
    protected String ip;
    
    /**
     * 操作系统类型
     */
    protected OsTypeEnum osType;
    
    /**
     * 用户名
     */
    protected String user;
    
    /**
     * 密码
     */
    protected String passwd;
    
    /**
     * 状态
     */
    protected StatusEnum status;

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public OsTypeEnum getOsType()
    {
        return osType;
    }

    public void setOsType(OsTypeEnum osType)
    {
        this.osType = osType;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPasswd()
    {
        return passwd;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    public StatusEnum getStatus()
    {
        return status;
    }

    public void setStatus(StatusEnum status)
    {
        this.status = status;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[IP = ").append(ip).append(", status = ").append(status);
        
        if (null != osType)
        {
            sb.append(", osType = ").append(osType);
        }
        
        if (null != user && !user.trim().isEmpty())
        {
            sb.append(", user = ").append(user);
        }
        if (null != passwd && !passwd.trim().isEmpty())
        {
            sb.append(", password = ").append("******");
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * 转换为JSONObject对象
     * @return JSONObject对象
     */
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", user);
        jsonObject.put("passwd", passwd);
        jsonObject.put("osType", osType);
        jsonObject.put("ip", ip);
        return jsonObject;
    }
    
    /**
     * 从JSONObject转换为MachineModel对象
     * @param jsonObj JSONObject对象
     * @return MachineModel对象
     */
    public static MachineModel fromJSONObject(JSONObject jsonObj)
    {
        if (null == jsonObj)
        {
            return null;
        }
        
        return (MachineModel) JSONObject.toBean(jsonObj, MachineModel.class);
    }
}
