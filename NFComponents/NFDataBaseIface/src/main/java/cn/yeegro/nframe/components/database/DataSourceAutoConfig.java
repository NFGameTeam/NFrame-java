package cn.yeegro.nframe.components.database;


import javax.sql.DataSource;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.database.iface.NFIDataBaseModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import cn.yeegro.nframe.components.database.aop.DataSourceAOP;
import cn.yeegro.nframe.components.database.constant.DataSourceKey;
import cn.yeegro.nframe.components.database.util.DynamicDataSource;




/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2017年04月23日 下午20:01:06 类说明


 * 在设置了spring.datasource.enable.dynamic 等于true是开启多数据源，配合日志
 */
@Configuration
@Import(DataSourceAOP.class)
@AutoConfigureBefore(value={DruidDataSourceAutoConfigure.class,MybatisPlusAutoConfiguration.class})
@ConditionalOnProperty(name = {"spring.datasource.dynamic.enable"}, matchIfMissing = false, havingValue = "true")
public class DataSourceAutoConfig {


    private NFIPluginManager pPluginManager;

    private NFIDataBaseModule m_pDataBaseModule;

    DataSourceAutoConfig(ApplicationContext context) {
        // 在初始化AutoConfiguration时会自动传入ApplicationContext
        SpringUtils.setAppContext(context);
        pPluginManager= NFPluginManager.GetSingletonPtr();
        //初始redis的时候是没有进行连接的空对象，需要调用CreateRedis(....)才能使用，见下面代码
        m_pDataBaseModule=pPluginManager.FindModule(NFIDataBaseModule.class);
    }

	 
//	创建数据源
//	所有引入db-core的模块都需要一个核心库，可以是user-center，也可以是oauth-center,file-center ,sms-center
	@Bean
	@ConfigurationProperties("spring.datasource.druid.core")
	public DataSource dataSourceCore(){
	    return m_pDataBaseModule.dataSourceCore();
	}
//	所有的核心库共享一个日志中心模块，改模块不采用mysql中的innodb引擎，采用归档引擎
	@Bean
	@ConfigurationProperties("spring.datasource.druid.log")
	public DataSource dataSourceLog(){
	    return m_pDataBaseModule.dataSourceLog();
	}
	
	
	@Primary
    @Bean // 只需要纳入动态数据源到spring容器
    public DataSource dataSource() {

        return m_pDataBaseModule.dataSource();
    }

    

    
    @Bean // 将数据源纳入spring事物管理
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource")  DataSource dataSource) {
        return m_pDataBaseModule.transactionManager(dataSource);
    }
   
}
