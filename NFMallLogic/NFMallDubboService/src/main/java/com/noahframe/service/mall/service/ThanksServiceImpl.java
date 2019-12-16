package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.model.TbThanks;
import com.noahframe.plugins.logic.mall.datas.DataTablesResult;
import com.noahframe.plugins.logic.mall.database.iface.ThanksService;
import org.springframework.stereotype.Service;

/**
 * @author Exrickx
 */
@Service
public class ThanksServiceImpl extends NFIBaseService implements ThanksService {


    @Override
    public DataTablesResult getThanksList() {
        ThanksService m_pThanksService=pPluginManager.FindModule(ThanksService.class);
        return m_pThanksService.getThanksList();
    }

    @Override
    public DataTablesResult getThanksListByPage(int page, int size) {
        ThanksService m_pThanksService=pPluginManager.FindModule(ThanksService.class);
        return m_pThanksService.getThanksListByPage(page, size);
    }

    @Override
    public Long countThanks() {
        ThanksService m_pThanksService=pPluginManager.FindModule(ThanksService.class);
        return m_pThanksService.countThanks();
    }

    @Override
    public int addThanks(TbThanks tbThanks) {
        ThanksService m_pThanksService=pPluginManager.FindModule(ThanksService.class);
        return m_pThanksService.addThanks(tbThanks);
    }

    @Override
    public int updateThanks(TbThanks tbThanks) {
        ThanksService m_pThanksService=pPluginManager.FindModule(ThanksService.class);
        return m_pThanksService.updateThanks(tbThanks);
    }

    @Override
    public int deleteThanks(int id) {
        ThanksService m_pThanksService=pPluginManager.FindModule(ThanksService.class);
        return m_pThanksService.deleteThanks(id);
    }

    @Override
    public TbThanks getThankById(int id) {
        ThanksService m_pThanksService=pPluginManager.FindModule(ThanksService.class);
        return m_pThanksService.getThankById(id);
    }
}
