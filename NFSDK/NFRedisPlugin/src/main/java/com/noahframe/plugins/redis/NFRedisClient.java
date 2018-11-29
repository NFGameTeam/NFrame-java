package com.noahframe.plugins.redis;

import com.noahframe.nfcore.iface.module.NFIRedisClient;
import redis.clients.jedis.*;

import java.util.List;

/**
 * @Author:zoocee
 * @Date:2018/11/8 14:32
 */
public class NFRedisClient extends NFRedis implements NFIRedisClient<Jedis>  {



    protected String mstrIP;
    protected int mnPort;
    protected String mstrAuthKey;

    private boolean bAuthed;
    private boolean bBusy;
    private Jedis m_pRedisClientSocket;

    public  NFRedisClient()
    {
        mnPort = 0;
        bBusy = false;
        bAuthed = false;
        m_pRedisClientSocket=null;
    }

    @Override
    public boolean Connecter(String ip, int port, String auth) {

        mstrIP = ip;
        mnPort = port;
        mstrAuthKey = auth;
        return false;
    }

    @Override
    public boolean Connecter(String ip, int port) {
        mstrIP = ip;
        mnPort = port;
        return true;
    }

    @Override
    public void SetClientCtx(Jedis client) {
        if (mstrAuthKey!=null&&bAuthed==false)
        {
            client.auth(mstrAuthKey);
            bAuthed=true;
        }
        m_pRedisClientSocket= client;
    }

    @Override
    public boolean Enable() {
        return m_pRedisClientSocket.isConnected();
    }

    @Override
    public boolean Authed() {

        return bAuthed;
    }

    @Override
    public boolean Busy() {
        return bBusy;
    }

    @Override
    public boolean KeepLive() {
        return false;
    }

    @Override
    public boolean Execute() {
        return false;
    }

    @Override
    public String GetAuthKey() {
        return mstrAuthKey;
    }

    @Override
    public String GetIP() {
        return mstrIP;
    }

    @Override
    public int GetPort() {
        return mnPort;
    }

    @Override
    public boolean ReConnect() {

        if (m_pRedisClientSocket!=null) {
            m_pRedisClientSocket.close();
        }
        return m_pRedisClientSocket.isConnected();

    }

    public boolean IsConnect()
    {
        if (m_pRedisClientSocket!=null)
        {
            return m_pRedisClientSocket.isConnected();
        }

        return false;
    }

    @Override
    public boolean AUTH(String password) {
        return false;
    }

    @Override
    public Jedis GetSingletonPtr() {
        return null;
    }
}
