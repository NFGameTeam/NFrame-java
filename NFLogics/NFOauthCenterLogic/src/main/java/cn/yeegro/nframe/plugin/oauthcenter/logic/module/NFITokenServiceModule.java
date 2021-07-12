package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.common.web.PageResult;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Map;

public interface NFITokenServiceModule extends NFIModule {

    //通用校验
    public void preCheckClient(String clientId, String clientSecret);
    //模拟客户端模式
    public OAuth2AccessToken getClientTokenInfo(String clientId, String clientSecret);
    //模拟密码模式
    public OAuth2AccessToken getUserTokenInfo(String clientId, String clientSecret, String username, String password);
    //模拟手机验证码模式
    public OAuth2AccessToken getMobileTokenInfo(String clientId, String clientSecret, String deviceId,
                                                String validCode);

    //刷新
    public OAuth2AccessToken getRefreshTokenInfo(String access_token)  ;
    //token list
    public PageResult<Map<String, String>> getTokenList(Map<String, Object> params) ;


    //移除
    public void removeToken(String access_token) ;

    public OAuth2AccessToken getTokenInfo(String access_token) ;

}
