/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface;

import com.noahframe.nfcore.api.plugin.PluginException;
import com.noahframe.nfcore.api.plugin.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will be extended by all plugins and
 * serve as the common class between a plugin and the application.
 *
 * @author Decebal Suiu
 */
public abstract class NFIPlugin {

    /**
     * Makes logging servicer available for descending classes.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Wrapper of the plugin.
     */
    protected PluginWrapper wrapper;

    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     */
    public NFIPlugin(final PluginWrapper wrapper) {
        if (wrapper == null) {
            throw new IllegalArgumentException("Wrapper cannot be null");
        }

        this.wrapper = wrapper;
    }

    /**
     * Retrieves the wrapper of this plug-in.
     */
    public final PluginWrapper getWrapper() {
        return wrapper;
    }

    public String GetPluginName(){
    	return wrapper.getPluginId();
    }
    public void Install() throws PluginException {
    	
    }
    public boolean Awake(){
    	
    	return true;
    }
    public boolean Init(){
    	return true;
    }
    public boolean AfterInit(){
    	return true;
    }
    public boolean CheckConfig(){
    	return true;
    }
    public boolean ReadyExecute(){
    	return true;
    }
    public boolean Execute(){
    	return true;
    }
    public boolean BeforeShut(){
    	return true;
    }
    public boolean Shut(){
    	return true;
    }
    public boolean Finalize(){
    	return true;
    }
    public boolean OnReloadPlugin(){
    	return true;
    }
    public void Uninstall()throws PluginException {
    	
    }
}
