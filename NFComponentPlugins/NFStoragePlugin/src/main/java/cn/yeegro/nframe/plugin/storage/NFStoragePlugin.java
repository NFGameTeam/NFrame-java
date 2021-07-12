package cn.yeegro.nframe.plugin.storage;

import org.pf4j.Plugin;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

public class NFStoragePlugin extends Plugin {

    public NFStoragePlugin(PluginWrapper wrapper) {
        super(wrapper);
      //  NFRedisModule.GetSingletonPtr().setCurrentPlugin(wrapper);
        // TODO Auto-generated constructor stub
    }

    /**
     * This method is called by the application when the plugin is started.
     * See {@link PluginManager#startPlugin(String)}.
     */
    public void start() {
        System.out.println("NFStoragePlugin.start()");
    }

    /**
     * This method is called by the application when the plugin is stopped.
     * See {@link PluginManager#stopPlugin(String)}.
     */
    public void stop() {
        System.out.println("NFStoragePlugin.stop()");
    }

    /**
     * This method is called by the application when the plugin is deleted.
     * See {@link PluginManager#deletePlugin(String)}.
     */
    public void delete() {
        System.out.println("NFStoragePlugin.delete()");
    }


}
