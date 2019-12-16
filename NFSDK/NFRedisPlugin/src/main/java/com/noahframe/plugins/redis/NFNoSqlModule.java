package com.noahframe.plugins.redis;

import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.api.plugin.Extension;
import com.noahframe.nfcore.iface.NFrame;
import com.noahframe.nfcore.iface.module.*;

import java.util.*;

/**
 * @Author:zoocee
 * @Date:2018/10/18 21:27
 */
@Extension
public class NFNoSqlModule extends NFINoSqlModule {

    protected NFPluginManager xPluginManager = null;
    public NFPluginManager pPluginManager = null;

    protected int mLastCheckTime;
    protected NFIClassModule m_pClassModule;
    protected NFIElementModule m_pElementModule;
    protected NFILogModule m_pLogModule;

    protected Map<String, NFIRedis> mxNoSqlDriver;

    protected NFRedisCluster mxNoSqlCluster;


    private static NFNoSqlModule SingletonPtr=null;

    public static NFNoSqlModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFNoSqlModule();
            return SingletonPtr;
        }
        else {
            return SingletonPtr;
        }
    }
    public NFNoSqlModule(){
        xPluginManager = NFPluginManager.GetSingletonPtr();
        pPluginManager = NFPluginManager.GetSingletonPtr();
        mxNoSqlDriver=new HashMap<>();
    }

    public boolean ConnectCluster()
    {
        return mxNoSqlCluster.Connecting();
    }
    public boolean Connect(String strIP, int nPort, String strPass) { return false; };
    public boolean Enable(){
        return false;
    }
    public boolean Busy(){
        return false;
    }
    public boolean KeepLive(){
        return false;
    }

    protected void CheckConnect(){

    }

    @Override
    public int AddConnectSql(String strID, String strIP) {
        return false;
    }

    @Override
    public int AddConnectSql(String strID, String strIP, int nPort) {

        if(!mxNoSqlDriver.containsKey(strID))
        {
            NFIRedisClient pNoSqlDriver = new NFRedisClient();
            pNoSqlDriver.Connecter(strIP, nPort);
            mxNoSqlDriver.put(strID,(NFIRedis) pNoSqlDriver);
            return mxNoSqlCluster.AddNode(pNoSqlDriver);
        }
        return false;
    }

    @Override
    public int AddConnectSql(String strID, String strIP, int nPort, String strPass) {

        if(!mxNoSqlDriver.containsKey(strID)) {
            NFIRedisClient pNoSqlDriver = new NFRedisClient();
            pNoSqlDriver.Connecter(strIP, nPort, strPass);
            mxNoSqlDriver.put(strID,(NFIRedis) pNoSqlDriver);
            return mxNoSqlCluster.AddNode(pNoSqlDriver);
        }
        return false;
    }

    @Override
    public List<String> GetDriverIdList() {
        return null;
    }

    @Override
    public NFIRedis GetDriver(String strID) {

        NFIRedis  xDriver= mxNoSqlDriver.get(strID);
        if (xDriver!=null && xDriver.Enable())
        {
            return xDriver;
        }

        return null;
    }

    @Override
    public NFIRedis GetDriverBySuitRandom() {

        Random r = new Random();
        int n=r.nextInt(mxNoSqlDriver.size()-1);
        int i=0;
        for (Map.Entry<String, NFIRedis> xDriver:mxNoSqlDriver.entrySet())
        {
           if (i<n)
           {
               i++;
               continue;
           }
            return xDriver.getValue();
        }

        return null;
    }

    @Override
    public NFIRedis GetDriverBySuitConsistent() {

//        for (Map.Entry<String, NFIRedisClient> entry : mxNoSqlDriver.entrySet()) {
//            NFIRedisClient  xDriver= entry.getValue();
//            if (xDriver!=null && xDriver.Enable())
//            {
//                return xDriver;
//            }
//        }
        return null;
    }

    @Override
    public NFIRedis GetDriverBySuit(String strHash) {

        return null;
    }

    @Override
    public boolean RemoveConnectSql(String strID) {



        return false;
    }

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
        mLastCheckTime = 0;
        return true;
    }

    @Override
    public boolean AfterInit() {
        mxNoSqlCluster=new NFRedisCluster();
        m_pClassModule = pPluginManager.FindModule(NFIClassModule.class);
        m_pElementModule = pPluginManager.FindModule(NFIElementModule.class);
        m_pLogModule = pPluginManager.FindModule(NFILogModule.class);

        NFIClass xLogicClass = m_pClassModule.GetElement(NFrame.NoSqlServer.ThisName());
        if (xLogicClass!=null)
        {
		    List<String> strIdList = xLogicClass.GetIDList();

            for (int i = 0; i < strIdList.size(); ++i)
            {
                String strId = strIdList.get(i);

                int nServerID = m_pElementModule.GetPropertyInt(strId, NFrame.NoSqlServer.ServerID());
                int nPort = m_pElementModule.GetPropertyInt(strId, NFrame.NoSqlServer.Port());
                String strIP = m_pElementModule.GetPropertyString(strId, NFrame.NoSqlServer.IP());
                String strAuth = m_pElementModule.GetPropertyString(strId, NFrame.NoSqlServer.Auth());

                if (this.AddConnectSql(strId, strIP, nPort, strAuth))
                {
                    String strLog;
                    strLog = "Connected NoSqlServer[" + strIP + "], Port = [" + nPort + "], Passsword = [" + strAuth + "]";
                    m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, new NFGUID(), strLog, m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());

                }
			else
                {
                    String strLog;
                    strLog = "Cannot connect NoSqlServer[" + strIP + "], Port = " + nPort + "], Passsword = [" + strAuth + "]";
                    m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), strLog,  m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
                }
            }
            this.ConnectCluster();
        }
        return true;
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

        Iterator<Map.Entry<String, NFIRedis>> iterator = mxNoSqlDriver.entrySet().iterator();
        while (iterator.hasNext()) {
            NFIRedis xNosqlDriver=iterator.next().getValue();
            xNosqlDriver.Execute();
        }
        CheckConnect();

        return true;
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
