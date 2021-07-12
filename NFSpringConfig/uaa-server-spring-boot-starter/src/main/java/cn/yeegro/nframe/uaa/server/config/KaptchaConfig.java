package cn.yeegro.nframe.uaa.server.config;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIKaptchaConfigerModule;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生成验证码配置
 * @date 2018-12-21 21:12:18


 */
@Configuration
public class KaptchaConfig {

    private NFIPluginManager pPluginManager;

    private NFIKaptchaConfigerModule m_pKaptchaConfigerModule;

    KaptchaConfig(ApplicationContext context) {
        // 在初始化AutoConfiguration时会自动传入ApplicationContext
        SpringUtils.setAppContext(context);
        pPluginManager= NFPluginManager.GetSingletonPtr();
        m_pKaptchaConfigerModule=pPluginManager.FindModule(NFIKaptchaConfigerModule.class);
    }

    @Bean
    public DefaultKaptcha producer() {
        return m_pKaptchaConfigerModule.KaptchaConfiger();
    }
}
