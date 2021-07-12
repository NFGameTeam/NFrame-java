package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.common.exception.service.ServiceException;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.common.web.PageResult;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenServiceModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.token.SmsCodeAuthenticationToken;
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.uaa.server.service.RedisClientDetailsService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.pf4j.Extension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.*;

@Extension
public class NFTokenServiceModule implements NFITokenServiceModule {


    private static NFTokenServiceModule SingletonPtr=null;

    public static NFTokenServiceModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFTokenServiceModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    public void preCheckClient(String clientId, String clientSecret) {
        if (clientId == null || "".equals(clientId)) {
            throw new UnapprovedClientAuthenticationException("请求参数中无clientId信息");
        }

        if (clientSecret == null || "".equals(clientSecret)) {
            throw new UnapprovedClientAuthenticationException("请求参数中无clientSecret信息");
        }
    }

    public OAuth2AccessToken getClientTokenInfo(String clientId, String clientSecret) {
        RedisClientDetailsService redisClientDetailsService= SpringUtils.getBean(RedisClientDetailsService.class);
        PasswordEncoder passwordEncoder= SpringUtils.getBean(PasswordEncoder.class);
        AuthorizationServerTokenServices authorizationServerTokenServices=SpringUtils.getBean(AuthorizationServerTokenServices.class);
        try {
            OAuth2AccessToken oauth2AccessToken = null;
            this.preCheckClient(clientId, clientSecret);
            ClientDetails clientDetails = redisClientDetailsService.loadClientByClientId(clientId);

            if (clientDetails == null) {
                throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
            } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
                throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
            }

            Map<String, String> map = new HashMap<>();
            map.put("client_secret", clientSecret);
            map.put("client_id", clientId);
            map.put("grant_type", "client_credentials");

            TokenRequest tokenRequest = new TokenRequest(map, clientId, clientDetails.getScope(), "client_credentials");

            OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(redisClientDetailsService);
            ClientCredentialsTokenGranter clientCredentialsTokenGranter = new ClientCredentialsTokenGranter(
                    authorizationServerTokenServices, redisClientDetailsService, requestFactory);

            clientCredentialsTokenGranter.setAllowRefresh(true);
            oauth2AccessToken = clientCredentialsTokenGranter.grant("client_credentials", tokenRequest);

            return oauth2AccessToken;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    public OAuth2AccessToken getUserTokenInfo(String clientId, String clientSecret, String username, String password) {
        RedisClientDetailsService redisClientDetailsService= SpringUtils.getBean(RedisClientDetailsService.class);
        PasswordEncoder passwordEncoder= SpringUtils.getBean(PasswordEncoder.class);
        AuthorizationServerTokenServices authorizationServerTokenServices=SpringUtils.getBean(AuthorizationServerTokenServices.class);
        AuthenticationManager authenticationManager=SpringUtils.getBean(AuthenticationManager.class);
        try {
            OAuth2AccessToken oauth2AccessToken = null;
            this.preCheckClient(clientId, clientSecret);
            ClientDetails clientDetails = redisClientDetailsService.loadClientByClientId(clientId);

            if (clientDetails == null) {
                throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
            } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
                throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
            }

            TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(),
                    "customer");

            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            OAuth2Authentication oauth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

            oauth2AccessToken = authorizationServerTokenServices.createAccessToken(oauth2Authentication);

            oauth2Authentication.setAuthenticated(true);

            return oauth2AccessToken;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public OAuth2AccessToken getMobileTokenInfo(String clientId, String clientSecret, String deviceId,
                                                String validCode) {
        RedisClientDetailsService redisClientDetailsService= SpringUtils.getBean(RedisClientDetailsService.class);
        PasswordEncoder passwordEncoder= SpringUtils.getBean(PasswordEncoder.class);

        try {
            OAuth2AccessToken oauth2AccessToken = null;

            this.preCheckClient(clientId, clientSecret);
            ClientDetails clientDetails = redisClientDetailsService.loadClientByClientId(clientId);

            if (clientDetails == null) {
                throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
            } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
                throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
            }

            TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(),
                    "customer");

            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

            SmsCodeAuthenticationToken token = new SmsCodeAuthenticationToken(deviceId);

            AuthenticationManager authenticationManager = SpringUtils.getBean(AuthenticationManager.class);

            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            OAuth2Authentication oauth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

            AuthorizationServerTokenServices authorizationServerTokenServices = SpringUtils
                    .getBean("defaultAuthorizationServerTokenServices", AuthorizationServerTokenServices.class);

            oauth2AccessToken = authorizationServerTokenServices.createAccessToken(oauth2Authentication);

            oauth2Authentication.setAuthenticated(true);
            return oauth2AccessToken;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public PageResult<Map<String, String>> getTokenList(Map<String, Object> params) {
        RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);
        TokenStore tokenStore = SpringUtils.getBean(TokenStore.class);
        try {
            List<Map<String, String>> list = new ArrayList<>();
            // Set<String> keys = Optional.ofNullable(redisTemplate.keys("access:" +
            // "*")).orElse(Sets.newHashSet(""));
            // 根据分页参数获取对应数据
            List<String> keys = findKeysForPage("access:" + "*", MapUtils.getInteger(params, "page"),
                    MapUtils.getInteger(params, "limit"));

            for (Object key : keys.toArray()) {
                // String key = page;
                // String accessToken = StringUtils.substringAfter(key, "access:");
                // OAuth2AccessToken token =
                // tokenStore.readAccessToken(accessToken);
                OAuth2AccessToken token = (OAuth2AccessToken) redisTemplate.opsForValue().get(key);
                HashMap<String, String> map = new HashMap<String, String>();


                if (token != null) {
                    map.put("token_type", token.getTokenType());
                    map.put("token_value", token.getValue());
                    map.put("expires_in", token.getExpiresIn() + "");
                }
                OAuth2Authentication oAuth2Auth = tokenStore.readAuthentication(token);
                Authentication authentication = oAuth2Auth.getUserAuthentication();

                map.put("client_id", oAuth2Auth.getOAuth2Request().getClientId());
                map.put("grant_type", oAuth2Auth.getOAuth2Request().getGrantType());

                if (authentication instanceof UsernamePasswordAuthenticationToken) {
                    UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

                    if (authenticationToken.getPrincipal() instanceof LoginAppUser) {
                        LoginAppUser user = (LoginAppUser) authenticationToken.getPrincipal();
                        map.put("user_id", user.getId() + "");
                        map.put("user_name", user.getUsername() + "");
                        map.put("user_head_imgurl", user.getAvatarPath() + "");
                    }

                } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                    // 刷新token方式
                    PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
                    if (authenticationToken.getPrincipal() instanceof LoginAppUser) {
                        LoginAppUser user = (LoginAppUser) authenticationToken.getPrincipal();
                        map.put("user_id", user.getId() + "");
                        map.put("user_name", user.getUsername() + "");
                        map.put("user_head_imgurl", user.getAvatarPath() + "");
                    }

                }
                list.add(map);

            }

            return PageResult.<Map<String, String>>builder().data(list).code(0).count((long) keys.size()).build();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    // 支持单机 集群模式替换keys *的危险操作
    public List<String> findKeysForPage(String patternKey, int pageNum, int pageSize) {
        RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);

        try {
            Set<String> execute = redisTemplate.execute(new RedisCallback<Set<String>>() {

                @Override
                public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {

                    Set<String> binaryKeys = new HashSet<>();

                    Cursor<byte[]> cursor = connection
                            .scan(new ScanOptions.ScanOptionsBuilder().match(patternKey).count(1000).build());
                    int tmpIndex = 0;
                    int startIndex = (pageNum - 1) * pageSize;
                    int end = pageNum * pageSize;
                    while (cursor.hasNext()) {
                        if (tmpIndex >= startIndex && tmpIndex < end) {
                            binaryKeys.add(new String(cursor.next()));
                            tmpIndex++;
                            continue;
                        }

                        // 获取到满足条件的数据后,就可以退出了
                        if (tmpIndex >= end) {
                            break;
                        }

                        tmpIndex++;
                        cursor.next();
                    }
                    connection.close();
                    return binaryKeys;
                }
            });

            List<String> result = new ArrayList<String>(pageSize);

            Optional.ofNullable(result).orElse(Lists.newArrayList("")).addAll(execute);
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public OAuth2AccessToken getRefreshTokenInfo(String access_token) {
        TokenStore tokenStore = SpringUtils.getBean(TokenStore.class);
        try {
            // 拿到当前用户信息
            OAuth2AccessToken oAuth2AccessToken = null;
            Authentication user = SecurityContextHolder.getContext().getAuthentication();

            if (user != null) {
                if (user instanceof OAuth2Authentication) {
                    Authentication athentication = (Authentication) user;
                    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails();
                }

            }
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
            OAuth2Authentication auth = (OAuth2Authentication) user;
            RedisClientDetailsService clientDetailsService = SpringUtils.getBean(RedisClientDetailsService.class);

            if (auth != null) {
                ClientDetails clientDetails = clientDetailsService
                        .loadClientByClientId(auth.getOAuth2Request().getClientId());

                AuthorizationServerTokenServices authorizationServerTokenServices = SpringUtils
                        .getBean("defaultAuthorizationServerTokenServices", AuthorizationServerTokenServices.class);
                OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);

                RefreshTokenGranter refreshTokenGranter = new RefreshTokenGranter(authorizationServerTokenServices,
                        clientDetailsService, requestFactory);

                Map<String, String> map = new HashMap<>();
                map.put("grant_type", "refresh_token");
                map.put("refresh_token", accessToken.getRefreshToken().getValue());
                TokenRequest tokenRequest = new TokenRequest(map, auth.getOAuth2Request().getClientId(),
                        auth.getOAuth2Request().getScope(), "refresh_token");

                oAuth2AccessToken = refreshTokenGranter.grant("refresh_token", tokenRequest);

                tokenStore.removeAccessToken(accessToken);

            }

            return oAuth2AccessToken;
        } catch (InvalidClientException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public void removeToken(String access_token) {
        TokenStore tokenStore = SpringUtils.getBean(TokenStore.class);
        try {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
            if (accessToken != null) {
                // 移除access_token
                tokenStore.removeAccessToken(accessToken);

                // 移除refresh_token
                if (accessToken.getRefreshToken() != null) {
                    tokenStore.removeRefreshToken(accessToken.getRefreshToken());
                }

            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public OAuth2AccessToken getTokenInfo(String access_token) {
        TokenStore tokenStore = SpringUtils.getBean(TokenStore.class);
        try {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
            return accessToken;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
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
}
