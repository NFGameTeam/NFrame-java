package com.noahframe.plugins.redis;

import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.iface.NFIPluginManager;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.module.NFILogModule;
import com.noahframe.nfcore.iface.module.NFIRedisClient;
import com.noahframe.nfcore.iface.module.NFIRedisCluster;
import redis.clients.jedis.*;

import java.util.*;

/**
 * @Author:zoocee
 * @Date:2018/11/12 11:38
 */
public class NFRedisCluster implements NFIRedisCluster<NFIRedisClient> {

    private JedisPoolConfig m_pRedisPoolConfig;
    private  Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
    private Map<String,NFIRedisClient> clients=new HashMap<>();
    private JedisCluster m_pCluster;

    private NFIPluginManager pPluginManager;
    protected NFILogModule m_pLogModule;

    public NFRedisCluster()
    {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pLogModule = pPluginManager.FindModule(NFILogModule.class);

        m_pRedisPoolConfig=new JedisPoolConfig();
        // 最大连接数
        m_pRedisPoolConfig.setMaxTotal(1);
        // 最大空闲数
        m_pRedisPoolConfig.setMaxIdle(1);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        // Could not get a resource from the pool
        m_pRedisPoolConfig.setMaxWaitMillis(1000);

    }

    @Override
    public boolean AddNode(NFIRedisClient redis) {
        boolean isAdd=false;
        HostAndPort node= new HostAndPort(redis.GetIP(), redis.GetPort());
        if (!nodes.contains(node))
        {
            nodes.add(node);
            clients.put(redis.GetIP()+":"+redis.GetPort(),redis);
            isAdd=true;
        }
        return isAdd;
    }

    @Override
    public boolean Connecting() {

        m_pCluster =         new JedisCluster(nodes,1500,1500,
                100, "NoahGameFrame", m_pRedisPoolConfig);

        boolean isConnected = false;
        if (m_pCluster != null) {
            isConnected=isConnecting();
        }

        return isConnected;
    }

    @Override
    public boolean isConnecting() {

        Map<String,JedisPool> pools= m_pCluster.getClusterNodes();
        boolean isConnected = false;
        Iterator<Map.Entry<String,JedisPool>> entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String,JedisPool> entry = entries.next();
            NFIRedisClient client=clients.get(entry.getKey());
            String strLog;
            strLog = "Redis Node[" + entry.getKey() + "],  Connected [" + !entry.getValue().isClosed() + "]";
            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, new NFGUID(), strLog,  m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
            client.SetClientCtx(entry.getValue().getResource());
            isConnected |=!entry.getValue().isClosed();
        }
        return isConnected;
    }


    public NFIRedisClient GetClientPtr() {
        NFIRedisClient client=null;
        Map<String,JedisPool> pools= m_pCluster.getClusterNodes();
        Iterator<Map.Entry<String,JedisPool>> entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String,JedisPool> entry = entries.next();
            String strLog;
            strLog = "Redis Node[" + entry.getKey() + "],  Connected [" + !entry.getValue().isClosed() + "]";
            m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, new NFGUID(), strLog,  m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
            if ( !entry.getValue().isClosed())
            {
                client=clients.get(entry.getKey());
                break;
            }
        }
        return client;
    }
}
