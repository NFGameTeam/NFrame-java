package com.noahframe.service.mall.service;


import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.model.TbItem;
import com.noahframe.plugins.logic.mall.datas.DataTablesResult;
import com.noahframe.plugins.logic.mall.database.pojo.ItemDto;
import com.noahframe.plugins.logic.mall.database.iface.ItemService;
import org.springframework.stereotype.Service;


/**
 * Created by Exrick on 2017/7/29.
 */
@Service
public class ItemServiceImpl extends NFIBaseService implements ItemService {


    @Override
    public ItemDto getItemById(Long itemId) {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.getItemById(itemId);
    }

    @Override
    public TbItem getNormalItemById(Long id) {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.getNormalItemById(id);
    }

    @Override
    public DataTablesResult getItemList(int draw, int start, int length, int cid, String search, String orderCol, String orderDir) {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.getItemList(draw, start, length, cid, search, orderCol, orderDir);
    }

    @Override
    public DataTablesResult getItemSearchList(int draw, int start, int length, int cid, String search, String minDate, String maxDate, String orderCol, String orderDir) {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.getItemSearchList(draw, start, length, cid, search, minDate, maxDate, orderCol, orderDir);
    }

    @Override
    public DataTablesResult getAllItemCount() {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.getAllItemCount();
    }

    @Override
    public TbItem alertItemState(Long id, Integer state) {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.alertItemState(id, state);
    }

    @Override
    public int deleteItem(Long id) {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.deleteItem(id);
    }

    @Override
    public TbItem addItem(ItemDto itemDto) {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.addItem(itemDto);
    }

    @Override
    public TbItem updateItem(Long id, ItemDto itemDto) {
        ItemService m_pItemService=pPluginManager.FindModule(ItemService.class);
        return m_pItemService.updateItem(id, itemDto);
    }
}
