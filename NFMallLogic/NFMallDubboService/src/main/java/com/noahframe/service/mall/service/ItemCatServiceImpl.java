package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.model.TbItemCat;
import com.noahframe.plugins.logic.mall.datas.ZTreeNode;
import com.noahframe.plugins.logic.mall.database.iface.ItemCatService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Exrick
 * @date 2017/8/2
 */
@Service
public class ItemCatServiceImpl extends NFIBaseService implements ItemCatService {


    @Override
    public TbItemCat getItemCatById(Long id) {
        ItemCatService m_pItemCatService=pPluginManager.FindModule(ItemCatService.class);
        return m_pItemCatService.getItemCatById(id);
    }

    @Override
    public List<ZTreeNode> getItemCatList(int parentId) {
        ItemCatService m_pItemCatService=pPluginManager.FindModule(ItemCatService.class);
        return m_pItemCatService.getItemCatList(parentId);
    }

    @Override
    public int addItemCat(TbItemCat tbItemCat) {
        ItemCatService m_pItemCatService=pPluginManager.FindModule(ItemCatService.class);
        return m_pItemCatService.addItemCat(tbItemCat);
    }

    @Override
    public int updateItemCat(TbItemCat tbItemCat) {
        ItemCatService m_pItemCatService=pPluginManager.FindModule(ItemCatService.class);
        return m_pItemCatService.updateItemCat(tbItemCat);
    }

    @Override
    public void deleteItemCat(Long id) {
        ItemCatService m_pItemCatService=pPluginManager.FindModule(ItemCatService.class);
        m_pItemCatService.deleteItemCat(id);
    }

    @Override
    public void deleteZTree(Long id) {
        ItemCatService m_pItemCatService=pPluginManager.FindModule(ItemCatService.class);
        m_pItemCatService.deleteZTree(id);
    }
}
