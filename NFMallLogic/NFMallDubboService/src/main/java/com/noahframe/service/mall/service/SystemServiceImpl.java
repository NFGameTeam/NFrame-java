package com.noahframe.service.mall.service;


import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.model.TbBase;
import com.noahframe.plugins.logic.mall.database.model.TbLog;
import com.noahframe.plugins.logic.mall.database.model.TbOrderItem;
import com.noahframe.plugins.logic.mall.database.model.TbShiroFilter;
import com.noahframe.plugins.logic.mall.datas.DataTablesResult;
import com.noahframe.plugins.logic.mall.database.iface.SystemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Exrickx
 */
@Service
public class SystemServiceImpl extends NFIBaseService implements SystemService {

    @Override
    public List<TbShiroFilter> getShiroFilter() {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.getShiroFilter();
    }

    @Override
    public Long countShiroFilter() {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.countShiroFilter();
    }

    @Override
    public int addShiroFilter(TbShiroFilter tbShiroFilter) {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.addShiroFilter(tbShiroFilter);
    }

    @Override
    public int updateShiroFilter(TbShiroFilter tbShiroFilter) {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.updateShiroFilter(tbShiroFilter);
    }

    @Override
    public int deleteShiroFilter(int id) {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.deleteShiroFilter(id);
    }

    @Override
    public TbBase getBase() {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.getBase();
    }

    @Override
    public int updateBase(TbBase tbBase) {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.updateBase(tbBase);
    }

    @Override
    public TbOrderItem getWeekHot() {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.getWeekHot();
    }

    @Override
    public int addLog(TbLog tbLog) {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.addLog(tbLog);
    }

    @Override
    public DataTablesResult getLogList(int draw, int start, int length, String search, String orderCol, String orderDir) {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.getLogList(draw, start, length, search, orderCol, orderDir);
    }

    @Override
    public Long countLog() {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.countLog();
    }

    @Override
    public int deleteLog(int id) {
        SystemService m_pSystemService=pPluginManager.FindModule(SystemService.class);
        return m_pSystemService.deleteLog(id);
    }
}
