package cn.yeegro.nframe.uaa.server.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import cn.yeegro.nframe.plugin.oauthcenter.logic.auth.details.DefaultClientDetails;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.UaaConstant;

import lombok.extern.slf4j.Slf4j;


/**
 * @author 刘斌（改）
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 类说明
 * 将oauth_client_details表数据缓存到redis，这里做个缓存优化
 * layui模块中有对oauth_client_details的crud， 注意同步redis的数据
 * 注意对oauth_client_details清楚redis db部分数据的清空


 */
@Slf4j
@SuppressWarnings("all")
public class RedisClientDetailsService extends JdbcClientDetailsService {


    
    private static final String SELECT_CLIENT_DETAILS_SQL = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, " +
            "web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove ,if_limit, limit_count ,id " +
            "from oauth_client_details where client_id = ? and status = 1  ";
    // 扩展 默认的 ClientDetailsService, 增加逻辑删除判断( status = 1)
    private static final String SELECT_FIND_STATEMENT = "select client_id, client_secret,resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove ,if_limit, limit_count ,id  from oauth_client_details where status = 1 order by client_id " ;

   

    private RedisTemplate<String,Object> redisTemplate ;
    
    private final JdbcTemplate jdbcTemplate;
    

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisClientDetailsService(DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        setSelectClientDetailsSql(SELECT_CLIENT_DETAILS_SQL) ;
        setFindClientDetailsSql(SELECT_FIND_STATEMENT) ;
    }

    

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        ClientDetails clientDetails = null;

        try {
			// 先从redis获取
			String value = (String) redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).get(clientId);
			if (StringUtils.isBlank(value)) {
			    clientDetails = cacheAndGetClient(clientId);
			} else {
			    clientDetails = JSONObject.parseObject(value, BaseClientDetails.class);
			}
		} catch (Exception e) {
			log.error("clientId:{},{}", clientId, clientId );
            throw new InvalidClientException ("应用获取失败"){};
		}

        return clientDetails;
    }

    /**
     * 缓存client并返回client
     *
     * @param clientId
     * @return
     */
	private ClientDetails cacheAndGetClient(String clientId) {
        // 从数据库读取
        ClientDetails clientDetails = null ;
        
        try {
    			clientDetails = jdbcTemplate.queryForObject(SELECT_CLIENT_DETAILS_SQL, new ClientDetailsRowMapper(), clientId);

            if (clientDetails != null) {
                // 写入redis缓存
                redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).put(clientId, JSONObject.toJSONString(clientDetails));
                log.info("缓存clientId:{},{}", clientId, clientDetails);
            }
            
            
            
        }catch (EmptyResultDataAccessException e) {
        	log.error("clientId:{},{}", clientId, clientId );
            throw new AuthenticationException ("应用不存在"){};
		} catch (NoSuchClientException e){
        	log.error("clientId:{},{}", clientId, clientId );
            throw new AuthenticationException ("应用不存在"){};
        }catch (InvalidClientException e) {
        	throw new AuthenticationException ("应用状态不合法"){};
        }

        return clientDetails;
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        super.updateClientDetails(clientDetails);
        cacheAndGetClient(clientDetails.getClientId());
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        super.updateClientSecret(clientId, secret);
        cacheAndGetClient(clientId);
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        super.removeClientDetails(clientId);
        removeRedisCache(clientId);
    }

    /**
     * 删除redis缓存
     *
     * @param clientId
     */
    private void removeRedisCache(String clientId) {
        redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).delete(clientId);
    }

    /**
     * 将oauth_client_details全表刷入redis
     */
    public void loadAllClientToCache() {
        if (redisTemplate.hasKey(UaaConstant.CACHE_CLIENT_KEY)) {
            return;
        }
        log.info("将oauth_client_details全表刷入redis");

        List<ClientDetails> list = this.listClientDetails();
        if (CollectionUtils.isEmpty(list)) {
        	log.error("oauth_client_details表数据为空，请检查");
            return;
        }

        list.parallelStream().forEach(client -> {
            redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).put(client.getClientId(), JSONObject.toJSONString(client));
        });
    }
    
    
    /**
	 * 追加if_limit  limit_count
	 * DefaultClientDetails 
	 * 
	 */
    public List<ClientDetails> listClientDetails() {
    	
  		return  jdbcTemplate.query(SELECT_FIND_STATEMENT,  new ClientDetailsRowMapper());  
  	}
	private static class ClientDetailsRowMapper implements RowMapper<ClientDetails> {
		private cn.yeegro.nframe.uaa.server.json.JsonMapper mapper = createJsonMapper();

		public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			DefaultClientDetails details = new DefaultClientDetails(rs.getString(1), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(7), rs.getString(6));
			details.setClientSecret(rs.getString(2));
			if (rs.getObject(8) != null) {
				details.setAccessTokenValiditySeconds(rs.getInt(8));
			}
			if (rs.getObject(9) != null) {
				details.setRefreshTokenValiditySeconds(rs.getInt(9));
			}
			String json = rs.getString(10);
			if (json != null) {
				try {
					Map<String, Object> additionalInformation = mapper.read(json, Map.class);
					details.setAdditionalInformation(additionalInformation);
				}
				catch (Exception e) {
					log.warn("Could not decode JSON for additional information: " + details, e);
				}
			}
			String scopes = rs.getString(11);
			long ifLimit = rs.getLong(12) ;
			details.setIfLimit(ifLimit);
			long limitCount = rs.getLong(13) ;
			details.setLimitCount(limitCount);
			details.setId( rs.getLong(14));
			if (scopes != null) {
				details.setAutoApproveScopes(org.springframework.util.StringUtils.commaDelimitedListToSet(scopes));
			}
			return details;
		}
	}
	 
	/**
	 * json process
	 * @return
	 */
	private static cn.yeegro.nframe.uaa.server.json.JsonMapper createJsonMapper() {
		if (ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", null)) {
			return new cn.yeegro.nframe.uaa.server.json.JacksonMapper();
		}
		else if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
			return new cn.yeegro.nframe.uaa.server.json.Jackson2Mapper();
		}
		return new cn.yeegro.nframe.uaa.server.json.NotSupportedJsonMapper();
	}

	 
	 
}
