package com.noahframe.nfcore.iface.module;

import java.util.List;
import java.util.Map;

/**
 * @Author:zoocee
 * @Date:${Date} ${Time}
 */
public abstract class NFINoSqlModule implements NFIModule {

    public abstract int AddConnectSql(String strID, String strIP, String strPort, Map mCfg);
    public abstract int AddConnectSql(String strID, String strIP, String strPort, String strPass,Map mCfg);

    public abstract List<String> GetDriverIdList();
    public abstract NFIRedis  GetDriver(String strID);
    public abstract NFIRedis  GetDriverBySuitRandom();
    public abstract NFIRedis  GetDriverBySuitConsistent();
    public abstract NFIRedis  GetDriverBySuit(String strHash);

    public abstract boolean RemoveConnectSql(String strID);
}

