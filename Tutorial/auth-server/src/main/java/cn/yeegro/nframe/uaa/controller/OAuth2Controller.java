package cn.yeegro.nframe.uaa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenServiceModule;
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysPermission;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.yeegro.nframe.common.exception.controller.ControllerException;
import cn.yeegro.nframe.common.utils.ResponseUtil;
import cn.yeegro.nframe.common.web.PageResult;
import cn.yeegro.nframe.log.annotation.LogAnnotation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 owen
 * @version 创建时间：2018年4月28日 下午2:18:54 类说明
 */
@Slf4j
@RestController
@Api(tags = "OAuth API")

@SuppressWarnings("all")
public class OAuth2Controller {


    private NFIPluginManager pPluginManager;
    private NFIUserModule m_pSysUserModule;
    private NFITokenServiceModule m_pTokenServiceModule;


    @ApiOperation(value = "clientId获取token")
    @PostMapping("/oauth/client/token")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public void getClientTokenInfo() {


        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenServiceModule = pPluginManager.FindModule(NFITokenServiceModule.class);

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {
            String clientId = request.getHeader("client_id");
            String clientSecret = request.getHeader("client_secret");
            OAuth2AccessToken oAuth2AccessToken = m_pTokenServiceModule.getClientTokenInfo(clientId, clientSecret);

            ResponseUtil.renderJson(response, oAuth2AccessToken);

        } catch (Exception e) {

            Map<String, String> rsp = new HashMap<>();
            rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
            rsp.put("msg", e.getMessage());

            ResponseUtil.renderJsonError(response, rsp, HttpStatus.UNAUTHORIZED.value());

        }
    }

    @ApiOperation(value = "用户名密码获取token")
    @PostMapping("/oauth/user/token")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public void getUserTokenInfo(
            @ApiParam(required = true, name = "username", value = "账号") @RequestParam(value = "username") String username,
            @ApiParam(required = true, name = "password", value = "密码") @RequestParam(value = "password") String password) {

        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenServiceModule = pPluginManager.FindModule(NFITokenServiceModule.class);

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        try {

            String clientId = request.getHeader("client_id");
            String clientSecret = request.getHeader("client_secret");

            OAuth2AccessToken oAuth2AccessToken = m_pTokenServiceModule.getUserTokenInfo(clientId, clientSecret, username,
                    password);

            ResponseUtil.renderJson(response, oAuth2AccessToken);

        } catch (Exception e) {

            Map<String, String> rsp = new HashMap<>();
            rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
            rsp.put("msg", e.getMessage());
            ResponseUtil.renderJsonError(response, rsp, HttpStatus.UNAUTHORIZED.value());

        }
    }


    @PostMapping("/authentication/sms")
    public void getMobileInfo(
            @ApiParam(required = true, name = "deviceId", value = "手机号") @RequestParam(value = "deviceId") String deviceId,
            @ApiParam(required = true, name = "validCode", value = "验证码") @RequestParam(value = "validCode", required = false) String validCode) {

        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenServiceModule = pPluginManager.FindModule(NFITokenServiceModule.class);

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {
            String clientId = request.getHeader("client_id");
            String clientSecret = request.getHeader("client_secret");
            OAuth2AccessToken oAuth2AccessToken = m_pTokenServiceModule.getMobileTokenInfo(clientId, clientSecret, deviceId,
                    validCode);

            ResponseUtil.renderJson(response, oAuth2AccessToken);

        } catch (Exception e) {

            Map<String, String> rsp = new HashMap<>();
            rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
            rsp.put("msg", e.getMessage());

            ResponseUtil.renderJsonError(response, rsp, HttpStatus.UNAUTHORIZED.value());
        }
    }

    @ApiOperation(value = "access_token刷新token")
    @PostMapping(value = "/oauth/refresh/token", params = "access_token")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public void refreshTokenInfo(String access_token) {

        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenServiceModule = pPluginManager.FindModule(NFITokenServiceModule.class);

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {

            OAuth2AccessToken oAuth2AccessToken = m_pTokenServiceModule.getRefreshTokenInfo(access_token);

            ResponseUtil.renderJson(response, oAuth2AccessToken);

        } catch (Exception e) {
            Map<String, String> rsp = new HashMap<>();
            rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
            rsp.put("msg", e.getMessage());
            ResponseUtil.renderJsonError(response, rsp, HttpStatus.UNAUTHORIZED.value());
        }

    }

    /**
     * 移除access_token和refresh_token
     *
     * @param access_token
     */
    @ApiOperation(value = "移除token")
    @PostMapping(value = "/oauth/remove/token", params = "access_token")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public void removeToken(String access_token) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenServiceModule = pPluginManager.FindModule(NFITokenServiceModule.class);
        try {
            m_pTokenServiceModule.removeToken(access_token);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @ApiOperation(value = "获取token信息")
    @PostMapping(value = "/oauth/get/token", params = "access_token")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public OAuth2AccessToken getTokenInfo(String access_token) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenServiceModule = pPluginManager.FindModule(NFITokenServiceModule.class);
        try {
            return m_pTokenServiceModule.getTokenInfo(access_token);
        } catch (Exception e) {
            throw new ControllerException(e);
        }

    }

    /**
     * 当前登陆用户信息
     * security获取当前登录用户的方法是SecurityContextHolder.getContext().getAuthentication()
     * 这里的实现类是org.springframework.security.oauth2.provider.OAuth2Authentication
     *
     * @return
     */
    @ApiOperation(value = "当前登陆用户信息")
    @GetMapping(value = {"/oauth/userinfo"}, produces = "application/json") // 获取用户信息。/auth/user
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public Map<String, Object> getCurrentUserDetail() {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        try {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("code", "0");
            LoginAppUser loginUser = m_pSysUserModule.getLoginAppUser();
            userInfo.put("user", loginUser);
            List<SysPermission> permissions = new ArrayList<>();
            new ArrayList(loginUser.getAuthorities()).forEach(o -> {
                SysPermission sysPermission = new SysPermission();
                sysPermission.setCode(o.toString());
                permissions.add(sysPermission);
            });
//             userInfo.put("authorities",
//             AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
//             );
            userInfo.put("permissions", permissions);
            return userInfo;
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @ApiOperation(value = "token列表")
    @PostMapping("/oauth/token/list")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public PageResult<Map<String, String>> getTokenList(@RequestParam Map<String, Object> params) throws Exception {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pTokenServiceModule = pPluginManager.FindModule(NFITokenServiceModule.class);
        try {
            return m_pTokenServiceModule.getTokenList(params);
        } catch (Exception e) {
            throw new ControllerException(e);
        }

    }


}
