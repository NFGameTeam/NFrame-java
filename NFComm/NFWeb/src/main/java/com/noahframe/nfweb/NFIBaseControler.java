package com.noahframe.nfweb;

import com.noahframe.loader.NFPluginManager;

/**
 * @Author:zoocee
 * @Date:2019/1/31 9:57
 */
public class NFIBaseControler<T> {

    protected NFPluginManager pPluginManager = null;

    public NFIBaseControler() {
        pPluginManager = NFPluginManager.GetSingletonPtr();

    }
}