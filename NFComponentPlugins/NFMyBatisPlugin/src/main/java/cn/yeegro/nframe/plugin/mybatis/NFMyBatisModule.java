package cn.yeegro.nframe.plugin.mybatis;

import cn.yeegro.nframe.components.database.constant.DataSourceKey;
import cn.yeegro.nframe.components.database.iface.NFIDataBaseModule;
import cn.yeegro.nframe.components.database.util.DataSourceHolder;
import cn.yeegro.nframe.components.database.util.DynamicDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.aspectj.lang.JoinPoint;
import org.pf4j.Extension;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Extension
public class NFMyBatisModule extends NFIDataBaseModule {

    private static NFMyBatisModule SingletonPtr=null;


    public static NFMyBatisModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFMyBatisModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public DataSource dataSourceCore() {
        return DruidDataSourceBuilder.create().build();
    }

    @Override
    public DataSource dataSourceLog() {
        return DruidDataSourceBuilder.create().build();
    }

    @Override
    public DataSource dataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        DataSource coreDataSource =  dataSourceCore() ;
        DataSource logDataSource =  dataSourceLog();
        dataSource.addDataSource(DataSourceKey.core, coreDataSource);
        dataSource.addDataSource(DataSourceKey.log, logDataSource);
        dataSource.setDefaultTargetDataSource(coreDataSource);
        return dataSource;
    }

    @Override
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public void changeDataSource(JoinPoint point, cn.yeegro.nframe.components.database.annotation.DataSource ds) {
        String dsId = ds.name();
        try {
            DataSourceKey dataSourceKey = DataSourceKey.valueOf(dsId);
            DataSourceHolder.setDataSourceKey(dataSourceKey);
        } catch (Exception e) {
            //log.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        }
    }

    @Override
    public void restoreDataSource(JoinPoint point, cn.yeegro.nframe.components.database.annotation.DataSource ds) {
       // log.debug("Revert DataSource : {transIdo} > {}", ds.name(), point.getSignature());
        DataSourceHolder.clearDataSourceKey();
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
