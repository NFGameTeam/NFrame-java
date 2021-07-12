package cn.yeegro.nframe.client.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.yeegro.nframe.common.web.Result;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2018年4月5日 下午19:52:21
 */
@Slf4j
@RestController
@SuppressWarnings("all")
public class UserController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('sys:user:add11')")
    public String hello() {
        redisTemplate.opsForValue().set("hello", "owen");
        return "hello";
    }

    @RequestMapping(value = {"/users"}, produces = "application/json") // 获取用户信息。/auth/user
    public Map<String, Object> user(OAuth2Authentication user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        log.debug("认证详细信息:" + user.getUserAuthentication().getPrincipal().toString());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
        return userInfo;
    }

    @RequestMapping(value = {"/user"}, produces = "application/json") // 获取用户信息。/auth/user
    public Principal user(Principal user) {
        return user;
    }


    @GetMapping("/getVersion")
    public Result token() {
        String str = RandomUtil.randomString(24);
        StrBuilder token = new StrBuilder();
        token.append(str);
        redisTemplate.opsForValue().set(token.toString(), token.toString(), 300);
        return Result.succeed(token.toString(), "");
    }


}
