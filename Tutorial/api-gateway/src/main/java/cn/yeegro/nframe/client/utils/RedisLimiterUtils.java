package cn.yeegro.nframe.client.utils;

import java.time.LocalDate;
import java.util.UUID;

import javax.annotation.Resource;

import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import cn.yeegro.nframe.common.web.Result;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@SuppressWarnings("all")
public class RedisLimiterUtils {
    public static final String API_WEB_TIME_KEY = "time_key:";
    public static final String API_WEB_COUNTER_KEY = "counter_key:";
    private static final String EXCEEDS_LIMIT = "规定的时间内超出了访问的限制！";

    @Resource
    private NFIRedisModule redisModule;

    public Result IpRateLimiter(String ip, int limit, int timeout) {
        String identifier = UUID.randomUUID().toString();
        String time_key = "time_key:ip:" + ip;
        String counter_key = "counter_key:ip:" + ip;

        if (!redisModule.hasKey(time_key) || redisModule.getExpire(time_key) <= 0) {
            redisModule.set(time_key, identifier, timeout);
            redisModule.set(counter_key, 0);
        }
        if (redisModule.hasKey(time_key) && redisModule.incr(counter_key, 1) > limit) {
            log.info(EXCEEDS_LIMIT);
            return Result.failedWith(null, -1, EXCEEDS_LIMIT);
        }
        return Result.succeedWith(null, 0, "调用次数:" + redisModule.get(counter_key));
    }


    public Result clientRateLimiter(String clientid, int limit, int timeout) {
        String identifier = UUID.randomUUID().toString();
        String time_key = "time_key:clientid:" + clientid;
        String counter_key = "counter_key:clientid:" + clientid;
        if (!redisModule.hasKey(time_key) || redisModule.getExpire(time_key) <= 0) {
            redisModule.set(time_key, identifier, timeout);
            redisModule.set(counter_key, 0);
        }
        if (redisModule.hasKey(time_key) && redisModule.incr(counter_key, 1) > limit) {
            log.info(EXCEEDS_LIMIT);
            return Result.failedWith(null, -1, EXCEEDS_LIMIT);
        }
        return Result.succeedWith(null, 0, "调用次数:" + redisModule.get(counter_key));
    }


    public Result urlRateLimiter(String path, int limit, int timeout) {
        String identifier = UUID.randomUUID().toString();
        String time_key = "time_key:path:" + path;
        String counter_key = "counter_key:path:" + path;
        if (!redisModule.hasKey(time_key) || redisModule.getExpire(time_key) <= 0) {
            redisModule.set(time_key, identifier, timeout);
            redisModule.set(counter_key, 0);
        }
        if (redisModule.hasKey(time_key) && redisModule.incr(counter_key, 1) > limit) {
            log.info(EXCEEDS_LIMIT);
            return Result.failedWith(null, -1, EXCEEDS_LIMIT);
        }
        return Result.succeedWith(null, 0, "调用次数:" + redisModule.get(counter_key));
    }


    public Result clientPathRateLimiter(String clientid, String access_path, int limit, int timeout) {
        String identifier = UUID.randomUUID().toString();
        LocalDate today = LocalDate.now();
        String time_key = "time_key:clientid:" + clientid + ":path:" + access_path;
        String counter_key = "counter_key:clientid:" + clientid + ":path:" + access_path;

        if (!redisModule.hasKey(time_key) || redisModule.getExpire(time_key) <= 0) {
            redisModule.set(time_key, identifier, timeout);
            redisModule.set(counter_key, 0);
        }
        if (redisModule.hasKey(time_key) && redisModule.incr(counter_key, 1) > limit) {
            log.info(EXCEEDS_LIMIT);
            return Result.failedWith(null, -1, EXCEEDS_LIMIT);
        }
        return Result.succeedWith(null, 0, "调用次数:" + redisModule.get(counter_key));
    }


    public Result rateLimitOfDay(String clientid, String access_path, long limit) {
        String identifier = UUID.randomUUID().toString();
        LocalDate today = LocalDate.now();
        String time_key = "time_key:date:" + today + ":clientid:" + clientid + ":path:" + access_path;
        String counter_key = "counter_key:date:" + today + ":clientid:" + clientid + ":path:" + access_path;

        if (!redisModule.hasKey(time_key) || redisModule.getExpire(time_key) <= 0) {
            //当天首次访问，初始化访问计数=0，有效期24h
            redisModule.set(time_key, identifier, 24L * 60L * 60L);
            redisModule.set(counter_key, 0);
        }

        //累加访问次数， 超出配置的limit则返回错误
        if (redisModule.incr(counter_key, 1) > limit) {
            log.info("日内超出了访问的限制！");
            return Result.failedWith(null, -1, "日内超出了访问的限制!");
        }
        return Result.succeedWith(null, 0, "调用总次数:" + redisModule.get(counter_key));
    }


    public Result acquireRateLimiter(String clientid, String access_path, int limit, int timeout) {
        String identifier = UUID.randomUUID().toString();
        LocalDate today = LocalDate.now();
        String time_key = "time_key:date:" + today + ":clientid:" + clientid + ":path:" + access_path;
        String counter_key = "counter_key:date:" + today + ":clientid:" + clientid + ":path:" + access_path;

        if (!redisModule.hasKey(time_key) || redisModule.getExpire(time_key) <= 0) {
            redisModule.set(time_key, identifier, timeout);
            redisModule.set(counter_key, 0);
        }
        if (redisModule.hasKey(time_key) && redisModule.incr(counter_key, 1) > limit) {
            log.info(EXCEEDS_LIMIT);
            return Result.failedWith(null, -1, EXCEEDS_LIMIT);
        }
        return Result.succeedWith(null, 0, "调用次数:" + redisModule.get(counter_key));
    }


    public void save(String tokenType, String Token, int timeout) {
        redisModule.set(tokenType, Token, timeout);
    }


    public String getToken(String tokenType) {
        return redisModule.get(tokenType).toString();
    }


    public void saveObject(String key, Object obj, long timeout) {
        redisModule.set(key, obj, timeout);
    }


    public void saveObject(String key, Object obj) {
        redisModule.set(key, obj);
    }


    public Object getObject(String key) {
        return redisModule.get(key);
    }


    public void removeObject(String key) {
        redisModule.del(key);
    }
}
