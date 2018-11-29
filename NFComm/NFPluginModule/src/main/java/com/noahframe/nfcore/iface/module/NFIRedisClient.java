package com.noahframe.nfcore.iface.module;

import java.util.List;

/**
 * @Author:zoocee
 * @Date:${Date} ${Time}
 */
public interface NFIRedisClient<T> {

    public boolean Connecter( String ip,  int port,  String auth);
    public boolean Connecter( String ip,  int port);

    public void SetClientCtx(T client);


    public boolean Enable();
    public boolean Authed();
    public boolean Busy();

    public boolean KeepLive();
    public boolean Execute();


    public  String GetAuthKey();

    public  String GetIP();

    public  int GetPort();

    public boolean ReConnect();

    /**
     * @brie if you have setted a password for Redis, you much use AUTH cmd to connect to the server than you can use other cmds
     * @param password
     * @return success: true; fail: false
     */
    public boolean AUTH( String password);

}
