package cn.yeegro.nframe.plugin.config;


import cn.yeegro.nframe.comm.code.api.NFComponentManager;
import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.api.NFPropertyManager;
import cn.yeegro.nframe.comm.code.api.NFRecordManager;
import cn.yeegro.nframe.comm.code.module.NFIComponentManager;
import cn.yeegro.nframe.comm.code.module.NFIPropertyManager;
import cn.yeegro.nframe.comm.code.module.NFIRecordManager;

public class ElementConfigInfo {

	protected NFIPropertyManager m_pPropertyManager;
	protected NFIRecordManager m_pRecordManager;
	protected NFIComponentManager m_pComponentManager;

	public ElementConfigInfo() {
		m_pPropertyManager = new NFPropertyManager(new NFGUID());
		m_pRecordManager = new NFRecordManager(new NFGUID());
		m_pComponentManager = new NFComponentManager(new NFGUID());
	}

	public NFIPropertyManager GetPropertyManager() {
		return m_pPropertyManager;
	}

	public NFIRecordManager GetRecordManager() {
		return m_pRecordManager;
	}

	public NFIComponentManager GetComponentManager() {
		return m_pComponentManager;
	}
}
