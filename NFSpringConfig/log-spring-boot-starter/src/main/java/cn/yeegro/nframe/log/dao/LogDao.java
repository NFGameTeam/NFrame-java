package cn.yeegro.nframe.log.dao;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import cn.yeegro.nframe.plugin.oauthcenter.logic.model.SysLog;

/**
 * @author 刘斌（改）
 * @create 2017年7月2日
 * 保存日志
 * eureka-server配置不需要datasource,不会装配bean


 * 需要配置多数据源才可以支持
 */
@Mapper
@ConditionalOnBean(DataSource.class)
public interface LogDao {

	@Insert("insert into sys_log(username, module, params, remark, flag, create_time) values(#{username}, #{module}, #{params}, #{remark}, #{flag}, #{createTime})")
	int save(SysLog log);

}
