package cn.yeegro.nframe.components.database.iface;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

public abstract class NFIDataBaseModule implements NFIModule {

    public abstract DataSource dataSourceCore();

    public abstract DataSource dataSourceLog();

    public abstract DataSource dataSource();

    public abstract DataSourceTransactionManager transactionManager(DataSource dataSource);

    public abstract void changeDataSource(JoinPoint point, cn.yeegro.nframe.components.database.annotation.DataSource ds);

    public abstract void restoreDataSource(JoinPoint point, cn.yeegro.nframe.components.database.annotation.DataSource ds);
}
