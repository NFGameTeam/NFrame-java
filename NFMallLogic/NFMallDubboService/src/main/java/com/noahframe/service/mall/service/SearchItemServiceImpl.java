package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.datas.EsInfo;
import com.noahframe.plugins.logic.mall.database.iface.SearchItemService;
import org.springframework.stereotype.Service;
/**
 * @author Exrickx
 */
@Service
public class SearchItemServiceImpl  extends NFIBaseService implements SearchItemService {

	@Override
	public int importAllItems() {
		SearchItemService m_pSearchItemService=pPluginManager.FindModule(SearchItemService.class);
		return m_pSearchItemService.importAllItems();
	}

	@Override
	public EsInfo getEsInfo() {
		SearchItemService m_pSearchItemService=pPluginManager.FindModule(SearchItemService.class);
		return m_pSearchItemService.getEsInfo();
	}
}
