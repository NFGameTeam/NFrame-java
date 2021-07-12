package cn.yeegro.nframe.log.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import cn.yeegro.nframe.plugin.oauthcenter.logic.model.SysLog;
import cn.yeegro.nframe.components.database.annotation.DataSource;
import cn.yeegro.nframe.log.dao.LogDao;
import cn.yeegro.nframe.log.service.LogService;

/**
 * @author 刘斌（改）
 * 切换数据源，存储log-center


 */
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;

	@Async
	@Override
	@DataSource(name="log")
	public void save(SysLog log) {
		if (log.getCreateTime() == null) {
			log.setCreateTime(new Date());
		}
		if (log.getFlag() == null) {
			log.setFlag(Boolean.TRUE);
		}

		logDao.save(log);
	}

	 
}
