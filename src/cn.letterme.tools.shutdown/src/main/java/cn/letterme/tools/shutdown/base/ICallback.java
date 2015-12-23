package cn.letterme.tools.shutdown.base;

import cn.letterme.tools.shutdown.base.model.MachineModel;

/**
 * 回调接口
 * @author zhengzhengzhong@outlook.com
 * @since 1.0.0
 */
public interface ICallback
{
    /**
     * 执行回调
     * @param input 输入的机器模型
     * @return 回调
     */
    MachineModel callback(MachineModel input); 
}
