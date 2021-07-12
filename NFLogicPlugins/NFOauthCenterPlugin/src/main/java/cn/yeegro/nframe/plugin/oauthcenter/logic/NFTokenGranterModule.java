package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.functor.AUTHOR_CONFIG_EVENT;
import cn.yeegro.nframe.plugin.oauthcenter.logic.granter.PasswordEnhanceTokenGranter;
import cn.yeegro.nframe.plugin.oauthcenter.logic.granter.SMSCodeTokenGranter;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenGranterModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.service.NFIValidateCodeService;
import cn.yeegro.nframe.plugin.oauthcenter.logic.service.ValidateCodeService;
import org.pf4j.Extension;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.List;

@Extension
public class NFTokenGranterModule implements NFITokenGranterModule {

    private NFIPluginManager pPluginManager;

    private static NFTokenGranterModule SingletonPtr = null;

    public static NFTokenGranterModule GetSingletonPtr() {
        if (null == SingletonPtr) {
            SingletonPtr = new NFTokenGranterModule();
            return SingletonPtr;
        } else {
            return SingletonPtr;
        }
    }

    NFTokenGranterModule(){
        pPluginManager = NFPluginManager.GetSingletonPtr();
    }

    //客户端模式   GRANT_TYPE = "client_credentials";
    public AUTHOR_CONFIG_EVENT onClientCredentialsTokenGranter = new AUTHOR_CONFIG_EVENT() {

        @Override
        public TokenGranter opt(AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
            ClientCredentialsTokenGranter m_clientCredentialsTokenGranter = new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory);
            mx_TokenGranterList.add(m_clientCredentialsTokenGranter);
            return m_clientCredentialsTokenGranter;
        }
    };
    //密码模式	  GRANT_TYPE = "password";
    public AUTHOR_CONFIG_EVENT onPasswordEnhanceTokenGranter = new AUTHOR_CONFIG_EVENT() {
        @Override
        public TokenGranter opt(AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
            PasswordEnhanceTokenGranter m_PasswordEnhanceTokenGranter=new PasswordEnhanceTokenGranter(tokenServices, clientDetailsService, requestFactory);
            mx_TokenGranterList.add(m_PasswordEnhanceTokenGranter);
            return m_PasswordEnhanceTokenGranter;
        }
    };
    //授权码模式   GRANT_TYPE = "authorization_code";
    public AUTHOR_CONFIG_EVENT onAuthorizationCodeTokenGranter = new AUTHOR_CONFIG_EVENT() {

        @Override
        public TokenGranter opt(AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {

            AuthorizationCodeTokenGranter m_AuthorizationCodeTokenGranter=new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory);
            mx_TokenGranterList.add(m_AuthorizationCodeTokenGranter);
            return m_AuthorizationCodeTokenGranter;
        }
    };
    //刷新模式	  GRANT_TYPE = "refresh_token";
    public AUTHOR_CONFIG_EVENT onRefreshTokenGranter = new AUTHOR_CONFIG_EVENT() {

        @Override
        public TokenGranter opt(AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
            RefreshTokenGranter m_RefreshTokenGranter=new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory);
            mx_TokenGranterList.add(m_RefreshTokenGranter);
            return m_RefreshTokenGranter;
        }
    };
    //简易模式	  GRANT_TYPE = "implicit";
    public AUTHOR_CONFIG_EVENT onImplicitTokenGranter = new AUTHOR_CONFIG_EVENT() {
        @Override
        public TokenGranter opt(AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
            ImplicitTokenGranter m_ImplicitTokenGranter=new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory);
            mx_TokenGranterList.add(m_ImplicitTokenGranter);
            return m_ImplicitTokenGranter;
        }
    };
    //短信模式	  GRANT_TYPE = "sms"; 参考ResourceOwnerPasswordTokenGranter重写
    public AUTHOR_CONFIG_EVENT onSMSCodeTokenGranter = new AUTHOR_CONFIG_EVENT() {
        @Override
        public TokenGranter opt(AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
            SMSCodeTokenGranter m_SMSCodeTokenGranter=new SMSCodeTokenGranter(tokenServices, clientDetailsService, requestFactory);
            mx_TokenGranterList.add(m_SMSCodeTokenGranter);
            return m_SMSCodeTokenGranter;
        }
    };

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
        mx_ConfigEvents.add(onClientCredentialsTokenGranter);
        mx_ConfigEvents.add(onPasswordEnhanceTokenGranter);
        mx_ConfigEvents.add(onAuthorizationCodeTokenGranter);
        mx_ConfigEvents.add(onRefreshTokenGranter);
        mx_ConfigEvents.add(onImplicitTokenGranter);
        mx_ConfigEvents.add(onSMSCodeTokenGranter);
        return false;
    }

    @Override
    public boolean AfterInit() {
        return false;
    }

    @Override
    public boolean CheckConfig() {
        return false;
    }

    @Override
    public boolean ReadyExecute() {
        return false;
    }

    @Override
    public boolean Execute() {
        return false;
    }

    @Override
    public boolean BeforeShut() {
        return false;
    }

    @Override
    public boolean Shut() {
        return false;
    }

    @Override
    public boolean Finalize() {
        return false;
    }

    @Override
    public boolean OnReloadPlugin() {
        return false;
    }


    @Override
    public List<TokenGranter> getTokenGranterList() {
        return mx_TokenGranterList;
    }

    @Override
    public NFIValidateCodeService getValidateCodeService() {
        return  new ValidateCodeService();
    }
}
