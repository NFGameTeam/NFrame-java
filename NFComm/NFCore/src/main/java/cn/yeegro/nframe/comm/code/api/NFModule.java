package cn.yeegro.nframe.comm.code.api;

import org.pf4j.PluginWrapper;
import cn.yeegro.nframe.comm.code.module.NFIModule;

public abstract class NFModule<T> implements NFIModule<T> {

    private PluginWrapper wrapper;

    public PluginWrapper getCurrentPlugin()
    {
        return wrapper;
    }

    public void setCurrentPlugin(PluginWrapper plugin)
    {
        wrapper=plugin;
    }
}
