package cn.yeegro.nframe.uaa.server.provider;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenStoreConfigerModule;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
public class SmsCodeAuthenticationProvider implements  AuthenticationProvider {
    private NFIPluginManager pPluginManager;
    private NFITokenStoreConfigerModule m_pTokenStoreConfigerModule;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenStoreConfigerModule = pPluginManager.FindModule(NFITokenStoreConfigerModule.class);
        return m_pTokenStoreConfigerModule.SmsCodeAuthenticationProvider_authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenStoreConfigerModule = pPluginManager.FindModule(NFITokenStoreConfigerModule.class);
        return m_pTokenStoreConfigerModule.supports(aClass);
    }

}
