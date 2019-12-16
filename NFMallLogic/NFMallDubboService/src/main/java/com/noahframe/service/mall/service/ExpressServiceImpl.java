package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.model.TbExpress;
import com.noahframe.plugins.logic.mall.database.iface.ExpressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Exrickx
 */
@Service
public class ExpressServiceImpl extends NFIBaseService implements ExpressService {


    @Override
    public List<TbExpress> getExpressList() {
        ExpressService m_pDictService=pPluginManager.FindModule(ExpressService.class);
        return m_pDictService.getExpressList();
    }

    @Override
    public int addExpress(TbExpress tbExpress) {
        ExpressService m_pDictService=pPluginManager.FindModule(ExpressService.class);
        return m_pDictService.addExpress(tbExpress);
    }

    @Override
    public int updateExpress(TbExpress tbExpress) {
        ExpressService m_pDictService=pPluginManager.FindModule(ExpressService.class);
        return m_pDictService.updateExpress(tbExpress);
    }

    @Override
    public int delExpress(int id) {
        ExpressService m_pDictService=pPluginManager.FindModule(ExpressService.class);
        return m_pDictService.delExpress(id);
    }
}
