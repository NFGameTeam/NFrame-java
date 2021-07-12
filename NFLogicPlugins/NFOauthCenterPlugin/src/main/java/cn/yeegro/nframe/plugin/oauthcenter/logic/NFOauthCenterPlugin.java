package cn.yeegro.nframe.plugin.oauthcenter.logic;

import org.pf4j.Plugin;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

public class NFOauthCenterPlugin extends Plugin {

    public NFOauthCenterPlugin(PluginWrapper wrapper) {
        super(wrapper);
//        NFAutoConfigerModule.GetSingletonPtr().setCurrentPlugin(wrapper);
//        NFResourceConfigerModule.GetSingletonPtr().setCurrentPlugin(wrapper);
//        NFTokenGranterModule.GetSingletonPtr().setCurrentPlugin(wrapper);
        // TODO Auto-generated constructor stub
    }

    /**
     * This method is called by the application when the plugin is started.
     * See {@link PluginManager#startPlugin(String)}.
     */
    public void start() {
        System.out.println("NFOauthCenterPlugin.start()");
    }

    /**
     * This method is called by the application when the plugin is stopped.
     * See {@link PluginManager#stopPlugin(String)}.
     */
    public void stop() {
        System.out.println("NFOauthCenterPlugin.stop()");
    }

    /**
     * This method is called by the application when the plugin is deleted.
     * See {@link PluginManager#deletePlugin(String)}.
     */
    public void delete() {
        System.out.println("NFOauthCenterPlugin.delete()");
    }


}
