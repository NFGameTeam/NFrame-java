package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.hutool.core.bean.BeanUtil;
import cn.yeegro.nframe.common.exception.service.ServiceException;
import cn.yeegro.nframe.common.web.PageResult;
import cn.yeegro.nframe.common.web.Result;
import cn.yeegro.nframe.plugin.oauthcenter.logic.dao.SysClientDao;
import cn.yeegro.nframe.plugin.oauthcenter.logic.dao.SysClientServiceDao;
import cn.yeegro.nframe.plugin.oauthcenter.logic.dao.SysServiceDao;
import cn.yeegro.nframe.plugin.oauthcenter.logic.model.SysClient;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.UaaConstant;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIClientModule;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.Extension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Extension
public class NFClientModule implements NFIClientModule {

    private static NFClientModule SingletonPtr=null;

    public static NFClientModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFClientModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public Map getClient(String clientId) {

        RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);

        // 先从redis获取
        Map client = null;
        String value = (String) redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).get(clientId);
        // 没有从数据库获取并缓存
        if (StringUtils.isBlank(value)) {
            client = cacheAndGetClient(clientId);
        } else {
            client = JSONObject.parseObject(value, Map.class);
        }
        return client;
    }

    @Override
    public List<Map> listByClientId(Long clientId) {

        SysServiceDao sysServiceDao= SpringUtils.getBean(SysServiceDao.class);

        return sysServiceDao.listByClientId(clientId);
    }

    @Override
    public Result saveOrUpdate(SysClient sysClient) {

        PasswordEncoder passwordEncoder= SpringUtils.getBean(PasswordEncoder.class);
        SysClientDao sysClientDao= SpringUtils.getBean(SysClientDao.class);
        try {
            sysClient.setClientSecret(passwordEncoder.encode(sysClient.getClientSecretStr()));

            if (sysClient.getId() != null) {// 修改
                sysClientDao.updateByPrimaryKey(sysClient);
            } else {// 新增
                SysClient r = sysClientDao.getClient(sysClient.getClientId());
                if (r != null) {
                    return Result.failed(sysClient.getClientId()+"已存在");
                }
                sysClientDao.save(sysClient);
            }
            return Result.succeed("操作成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }



    @Override
    @Transactional
    public void delete(Long id) {
        RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);
        SysClientDao sysClientDao= SpringUtils.getBean(SysClientDao.class);
        SysClientServiceDao sysClientServiceDao= SpringUtils.getBean(SysClientServiceDao.class);
        try {
            SysClient client = sysClientDao.getById(id);
            sysClientDao.delete(id);
            sysClientServiceDao.delete(id,null);
            redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).delete(client.map().getClientId()) ;
//            log.debug("删除应用id:{}", id);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageResult<SysClient> list(Map<String, Object> params) {
        SysClientDao sysClientDao= SpringUtils.getBean(SysClientDao.class);
        try {
            //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
            PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);
            List<SysClient> list = sysClientDao.findList(params);
            PageInfo<SysClient> pageInfo = new PageInfo<>(list);
            return PageResult.<SysClient>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build()  ;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }
    public  SysClient getById(Long id) {
        SysClientDao sysClientDao= SpringUtils.getBean(SysClientDao.class);
        try {
            return sysClientDao.getById(id);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<SysClient> findList(Map<String, Object> params) {

        SysClientDao sysClientDao= SpringUtils.getBean(SysClientDao.class);
        return sysClientDao.findList(params);
    }



    @Override
    public Result updateEnabled(Map<String, Object> params) {
        RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);
        SysClientDao sysClientDao= SpringUtils.getBean(SysClientDao.class);
        try {
            Long id = MapUtils.getLong(params, "id");
            Boolean enabled = MapUtils.getBoolean(params, "status");
            SysClient client = sysClientDao.getById(id);
            if (client == null) {
                return Result.failed("应用不存在");
                //throw new IllegalArgumentException("用户不存在");
            }
            client.setStatus(enabled);

            int i = sysClientDao.updateByPrimaryKey(client) ;

            ClientDetails clientDetails = client.map();

            if(enabled){
                redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).put(client.getClientId(), JSONObject.toJSONString(clientDetails));
            }else{
                redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).delete(client.getClientId()) ;
            }

//            log.info("应用状态修改：{}", client);

            return i > 0 ? Result.succeed(client, "更新成功") : Result.failed("更新失败");
        } catch (InvalidClientException e) {
            throw new ServiceException(e);
        }
    }

    private Map cacheAndGetClient(String clientId) {
        RedisTemplate<String, Object> redisTemplate= SpringUtils.getBean(RedisTemplate.class);
        SysClientDao sysClientDao= SpringUtils.getBean(SysClientDao.class);

        Map client = null;
        try {
            client = sysClientDao.getClientServer(clientId);	// 从数据库读取
            if (client != null) {
                SysClient sysClient = BeanUtil.toBean(client, SysClient.class);
                // 写入redis缓存
                redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).put(clientId,
                        JSONObject.toJSONString(sysClient.map())); //缓存
//                log.info("缓存clientId:{},{}", clientId, sysClient);
            }
        } catch (Exception e) {
            throw new ServiceException("应用状态不合法") ;
        }

        return client;
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
