package com.noahframe.loader;

import com.noahframe.api.file.SysPath;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



public class NFPluginsStarter implements ServletContextListener  {

    private NFPluginManager m_NFPlugins=null;
    private boolean bExitApp=false;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        bExitApp=true;
        m_NFPlugins.BeforeShut();
        m_NFPlugins.Shut();
        m_NFPlugins.Finalize();
        m_NFPlugins.ReleaseInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        String plugins_root= SysPath.getPluginsRoot();
        System.out.print("当前插件根目录:"+plugins_root+"\n");
        System.setProperty("plugins.Dir", plugins_root);
        m_NFPlugins= NFPluginManager.GetSingletonPtr();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
        m_NFPlugins.setWebContext(ctx);
        m_NFPlugins.SetAppID(6);
        m_NFPlugins.Awake();
        m_NFPlugins.Init();
        m_NFPlugins.AfterInit();
        m_NFPlugins.CheckConfig();
        m_NFPlugins.ReadyExecute();


//        Thread plugin_manager=new Thread(
//                new Runnable() {
//                    public void run() {
//                        while (!bExitApp) {
//                            m_NFPlugins.Execute();
//                        }
//                    }
//                });
//        plugin_manager.setName("plugin_manager");
//        plugin_manager.start();
    }

}
