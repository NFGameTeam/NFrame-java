package cn.yeegro.nframe.uaa.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import cn.yeegro.nframe.common.exception.controller.ControllerException;
import cn.yeegro.nframe.log.annotation.LogAnnotation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用于数据源切换
 *
 * @author zhangzhiguang
 * @create 2018-08-24 20:38
 */
@Controller
@Api(tags = "REDIS API")
@RequestMapping("/redis")
@SuppressWarnings("all")
public class RedisController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @ResponseBody
    @GetMapping("/memoryInfo")
    @ApiOperation(value = "redis内存信息")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public String getMemoryInfo() {
        try {
            Map<String, Object> map = new HashMap<>();

            Object o = redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    return Optional.ofNullable(connection.info("memory")).orElseThrow(RuntimeException::new).get("used_memory");
                }
            });
            map.put("used_memory", o);
            map.put("create_time", System.currentTimeMillis());

            return JSON.toJSONString(map);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }


    @ResponseBody
    @GetMapping("/keysSize")
    @ApiOperation(value = "redis键值信息")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public String getKeysSize() {
        try {
            Map<String, Object> map = new HashMap<>();

            Object o = redisTemplate.execute(new RedisCallback() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.dbSize();
                }
            });
            ;
            map.put("dbSize", o);
            map.put("create_time", System.currentTimeMillis());

            return JSON.toJSONString(map);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

}
