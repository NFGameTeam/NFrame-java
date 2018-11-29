package com.noahframe.nfcore.iface.module;

import java.util.List;

/**
 * @Author:zoocee
 * @Date:${Date} ${Time}
 */
public abstract class NFINoSqlModule implements NFIModule {

    public abstract boolean AddConnectSql(String strID, String strIP);
    public abstract boolean AddConnectSql(String strID, String strIP, int nPort);
    public abstract boolean AddConnectSql(String strID, String strIP, int nPort, String strPass);

    public abstract List<String> GetDriverIdList();
    public abstract NFIRedisClient  GetDriver(String strID);
    public abstract NFIRedisClient  GetDriverBySuitRandom();
    public abstract NFIRedisClient  GetDriverBySuitConsistent();
    public abstract NFIRedisClient  GetDriverBySuit(String strHash);

    public abstract boolean RemoveConnectSql(String strID);
}
