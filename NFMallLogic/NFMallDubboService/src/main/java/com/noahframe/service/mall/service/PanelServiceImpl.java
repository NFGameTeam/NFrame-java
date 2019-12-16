package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.iface.PanelService;
import com.noahframe.plugins.logic.mall.database.model.TbPanel;
import com.noahframe.plugins.logic.mall.datas.ZTreeNode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zoocee
 * @Date:2019/10/11 14:48
 */
@Service
public class PanelServiceImpl extends NFIBaseService implements PanelService {
    @Override
    public TbPanel getTbPanelById(int id) {
        PanelService m_pPanelService=pPluginManager.FindModule(PanelService.class);

        return m_pPanelService.getTbPanelById(id);
    }

    @Override
    public List<ZTreeNode> getPanelList(int position, boolean showAll) {
        PanelService m_pPanelService=pPluginManager.FindModule(PanelService.class);
        return m_pPanelService.getPanelList(position, showAll);
    }

    @Override
    public int addPanel(TbPanel tbPanel) {
        PanelService m_pPanelService=pPluginManager.FindModule(PanelService.class);
        return m_pPanelService.addPanel(tbPanel);
    }

    @Override
    public int updatePanel(TbPanel tbPanel) {
        PanelService m_pPanelService=pPluginManager.FindModule(PanelService.class);
        return m_pPanelService.updatePanel(tbPanel);
    }

    @Override
    public int deletePanel(int id) {
        PanelService m_pPanelService=pPluginManager.FindModule(PanelService.class);
        return m_pPanelService.deletePanel(id);
    }
}
