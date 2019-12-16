package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.front.SearchResult;
import com.noahframe.plugins.logic.mall.database.iface.SearchService;
import org.springframework.stereotype.Service;



/**
 * @author Exrickx
 */
@Service
public class SearchServiceImpl  extends NFIBaseService implements SearchService {

	/**
	 * 使用QueryBuilder
	 * termQuery("key", obj) 完全匹配
	 * termsQuery("key", obj1, obj2..)   一次匹配多个值
	 * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
	 * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
	 */
	@Override
	public SearchResult search(String key, int page, int size, String sort, int priceGt, int priceLte) {

		SearchService m_pSearchItemService=pPluginManager.FindModule(SearchService.class);
		return m_pSearchItemService.search(key, page, size, sort, priceGt, priceLte);
	}

	@Override
	public String quickSearch(String key) {
		SearchService m_pSearchItemService=pPluginManager.FindModule(SearchService.class);
		return m_pSearchItemService.quickSearch(key);
	}
}
