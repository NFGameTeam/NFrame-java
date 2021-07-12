package cn.yeegro.nframe.components.database.aop;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.database.iface.NFIDataBaseModule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import cn.yeegro.nframe.components.database.annotation.DataSource;
import cn.yeegro.nframe.components.database.constant.DataSourceKey;
import cn.yeegro.nframe.components.database.util.DataSourceHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * 切换数据源Advice


 */
@Slf4j
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
public class DataSourceAOP {


    private NFIPluginManager pPluginManager;

    private NFIDataBaseModule m_pDataBaseModule;

    DataSourceAOP() {
        pPluginManager= NFPluginManager.GetSingletonPtr();
        //初始redis的时候是没有进行连接的空对象，需要调用CreateRedis(....)才能使用，见下面代码
        m_pDataBaseModule=pPluginManager.FindModule(NFIDataBaseModule.class);
    }


    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, DataSource ds) throws Throwable {
        m_pDataBaseModule.changeDataSource(point, ds);
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, DataSource ds) {
        m_pDataBaseModule.restoreDataSource(point, ds);
    }

}