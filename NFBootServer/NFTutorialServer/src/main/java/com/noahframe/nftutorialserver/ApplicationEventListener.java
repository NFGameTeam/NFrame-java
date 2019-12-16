package com.noahframe.nftutorialserver;

import com.noahframe.api.file.SysPath;
import com.noahframe.loader.NFPluginManager;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @Author:zoocee
 * @Date:2019/7/24 1:33
 */
public class ApplicationEventListener implements ApplicationListener{

    private NFPluginManager m_NFPlugins=null;
    private boolean bExitApp=false;
    private ApplicationContext Ctx;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 在这里可以监听到Spring Boot的生命周期
        if (event instanceof ApplicationEnvironmentPreparedEvent) { // 初始化环境变量
            System.out.println("初始化环境变量");
            String plugins_root= SysPath.getPluginsRoot();
            try {
                System.out.print("当前项目执行目录:" + SysPath.getClassRootPath() + "\n");
                System.out.print("当前插件根目录:" + plugins_root + "\n");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.setProperty("plugins.Dir", plugins_root);
            m_NFPlugins= NFPluginManager.GetSingletonPtr();
            m_NFPlugins.SetAppID(6);
            m_NFPlugins.Awake();

//            Thread plugin_manager=new Thread(
//                    new Runnable() {
//                        public void run() {
//                            while (!bExitApp) {
//                                m_NFPlugins.Execute();
//                                try {
//                                    Thread.sleep(3000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
//            plugin_manager.setName("plugin_manager");
//            plugin_manager.start();
        } else if (event instanceof ApplicationPreparedEvent) { // 初始化完成
            System.out.println("初始化完成");
        } else if (event instanceof ContextRefreshedEvent) { // 应用刷新
            System.out.println("应用刷新");
            this.Ctx=((ContextRefreshedEvent) event).getApplicationContext();
            if (Ctx!=null) {
                m_NFPlugins.setWebContext(Ctx);
                m_NFPlugins.Init();
            }

        } else if (event instanceof ApplicationReadyEvent) {// 应用已启动完成
            System.out.println("应用已启动完成");
            if (Ctx!=null) {
                m_NFPlugins.AfterInit();
                m_NFPlugins.CheckConfig();
                m_NFPlugins.ReadyExecute();
            }
        } else if (event instanceof ContextStartedEvent) { // 应用启动，需要在代码动态添加监听器才可捕获
            System.out.println("应用启动，需要在代码动态添加监听器才可捕获");
        } else if (event instanceof ContextStoppedEvent) { // 应用停止
            System.out.println("应用停止");
        } else if (event instanceof ContextClosedEvent) { // 应用关闭
            System.out.println("应用关闭");
        } else {
        }
    }

}
