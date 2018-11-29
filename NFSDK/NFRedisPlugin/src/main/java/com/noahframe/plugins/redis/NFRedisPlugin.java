package com.noahframe.plugins.redis;

import com.noahframe.nfcore.api.plugin.PluginWrapper;
import com.noahframe.nfcore.iface.NFIPlugin;

/**
 * @Author:zoocee
 * @Date:2018/10/18 21:27
 */
public class NFRedisPlugin extends NFIPlugin {

    public NFRedisPlugin(PluginWrapper wrapper) {
        super(wrapper);

    }

    @Override
    public void Install() {
        System.out.println("NFRedisPlugin.Install()");
    }

    @Override
    public void Uninstall() {
        System.out.println("NFRedisPlugin.Uninstall()");
    }
}