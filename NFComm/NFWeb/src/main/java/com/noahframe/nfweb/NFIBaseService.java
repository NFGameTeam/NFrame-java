package com.noahframe.nfweb;

import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.iface.module.NFIRedis;

/**
 * @Author:zoocee
 * @Date:2019/2/1 9:02
 */
public class NFIBaseService<T> {

    protected NFPluginManager pPluginManager = null;
    protected NFIRedis jedisClient;

    public NFIBaseService() {
        pPluginManager = NFPluginManager.GetSingletonPtr();

    }
}
