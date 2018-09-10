package com.noahframe.plugins.config;


import com.noahframe.nfcore.api.core.NFComponentManager;
import com.noahframe.nfcore.api.core.NFPropertyManager;
import com.noahframe.nfcore.api.core.NFRecordManager;
import com.noahframe.nfcore.iface.module.NFIComponentManager;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.module.NFIPropertyManager;
import com.noahframe.nfcore.iface.module.NFIRecordManager;

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
