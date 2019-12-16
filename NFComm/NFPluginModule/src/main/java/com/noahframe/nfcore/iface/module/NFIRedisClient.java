package com.noahframe.nfcore.iface.module;

import java.util.List;

/**
 * @Author:zoocee
 * @Date:${Date} ${Time}
 */
public abstract class NFIRedisClient<T> extends NFIRedis<T> {

    public abstract boolean Connecter( String ip,  int port,  String auth);
    public abstract boolean Connecter( String ip,  int port);

    public abstract void SetClientCtx(T client);


    public abstract boolean Enable();
    public abstract boolean Authed();
    public abstract boolean Busy();

    public abstract boolean KeepLive();
    public abstract boolean Execute();


    public abstract String GetAuthKey();

    public abstract String GetIP();

    public abstract int GetPort();

    public abstract boolean ReConnect();

    /**
     * @brie if you have setted a password for Redis, you much use AUTH cmd to connect to the server than you can use other cmds
     * @param password
     * @return success: true; fail: false
     */
    public abstract boolean AUTH( String password);

}
