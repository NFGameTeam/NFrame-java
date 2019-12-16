package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.model.TbDict;
import com.noahframe.plugins.logic.mall.database.iface.DictService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Exrickx
 */
@Service
public class DictServiceImpl extends NFIBaseService implements DictService {


    @Override
    public List<TbDict> getDictList() {
        DictService m_pDictService=pPluginManager.FindModule(DictService.class);
        return m_pDictService.getDictList();
    }

    @Override
    public List<TbDict> getStopList() {
        DictService m_pDictService=pPluginManager.FindModule(DictService.class);
        return m_pDictService.getStopList();
    }

    @Override
    public int addDict(TbDict tbDict) {
        DictService m_pDictService=pPluginManager.FindModule(DictService.class);
        return m_pDictService.addDict(tbDict);
    }

    @Override
    public int updateDict(TbDict tbDict) {
        DictService m_pDictService=pPluginManager.FindModule(DictService.class);
        return m_pDictService.updateDict(tbDict);
    }

    @Override
    public int delDict(int id) {
        DictService m_pDictService=pPluginManager.FindModule(DictService.class);
        return m_pDictService.delDict(id);
    }
}
