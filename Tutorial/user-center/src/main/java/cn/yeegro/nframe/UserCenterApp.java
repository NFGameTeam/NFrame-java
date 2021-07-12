/**
 *
 */
package cn.yeegro.nframe;

import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.annotation.EnableApiIdempotent;
import cn.yeegro.nframe.tools.file.SysPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cn.yeegro.nframe.common.port.PortApplicationEnvironmentPreparedEventListener;
import cn.yeegro.nframe.log.annotation.EnableLogging;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 作者 刘斌（改）
 * @version 创建时间：2018年4月5日 下午19:52:21
 * 类说明
 */

@ComponentScan({"cn.yeegro.nframe","cn.yeegro.nframe.plugin.usercenter.logic"})
@EnableFeignClients(basePackages = {"cn.yeegro.nframe.plugin.usercenter.logic.feign"})
@Configuration
@EnableLogging
@EnableDiscoveryClient
@SpringBootApplication
@EnableApiIdempotent
@EnableTransactionManagement
public class UserCenterApp {

    public static NFPluginManager m_NFPlugins = null;

    public static void main(String[] args) {
//		固定端口启动
//		SpringApplication.run(UserCenterApp.class, args);

        System.out.println("动态获取配置文件");
        String plugins_root = SysPath.getPluginsRoot();
        System.out.print("当前插件根目录:" + plugins_root + "\n");
        System.setProperty("plugins.Dir", plugins_root);
        m_NFPlugins = NFPluginManager.GetSingletonPtr();
        m_NFPlugins.SetAppID(6);
        m_NFPlugins.Awake();
        m_NFPlugins.Init();
        m_NFPlugins.AfterInit();
        m_NFPlugins.CheckConfig();
        m_NFPlugins.ReadyExecute();

        //随机端口启动
        SpringApplication app = new SpringApplication(UserCenterApp.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);

    }

}
