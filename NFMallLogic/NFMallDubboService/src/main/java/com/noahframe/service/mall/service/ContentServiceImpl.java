package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.front.AllGoodsResult;
import com.noahframe.plugins.logic.mall.database.front.ProductDet;
import com.noahframe.plugins.logic.mall.database.iface.ContentService;
import com.noahframe.plugins.logic.mall.database.model.TbPanel;
import com.noahframe.plugins.logic.mall.database.model.TbPanelContent;
import com.noahframe.plugins.logic.mall.datas.DataTablesResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zoocee
 * @Date:2019/10/11 14:49
 */
@Service
public class ContentServiceImpl extends NFIBaseService implements ContentService {
    @Override
    public int addPanelContent(TbPanelContent tbPanelContent) {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.addPanelContent(tbPanelContent);
    }

    @Override
    public DataTablesResult getPanelContentListByPanelId(int panelId) {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getPanelContentListByPanelId(panelId);
    }

    @Override
    public int deletePanelContent(int id) {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.deletePanelContent(id);
    }

    @Override
    public int updateContent(TbPanelContent tbPanelContent) {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.updateContent(tbPanelContent);
    }

    @Override
    public TbPanelContent getTbPanelContentById(int id) {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getTbPanelContentById(id);
    }

    @Override
    public List<TbPanel> getHome() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getHome();
    }

    @Override
    public List<TbPanel> getRecommendGoods() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getRecommendGoods();
    }

    @Override
    public List<TbPanel> getThankGoods() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getThankGoods();
    }

    @Override
    public ProductDet getProductDet(Long id) {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getProductDet(id);
    }

    @Override
    public AllGoodsResult getAllProduct(int page, int size, String sort, Long cid, int priceGt, int priceLte) {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getAllProduct(page, size, sort, cid, priceGt, priceLte);
    }

    @Override
    public String getIndexRedis() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getIndexRedis();
    }

    @Override
    public int updateIndexRedis() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.updateIndexRedis();
    }

    @Override
    public String getRecommendRedis() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getRecommendRedis();
    }

    @Override
    public int updateRecommendRedis() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.updateRecommendRedis();
    }

    @Override
    public String getThankRedis() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getThankRedis();
    }

    @Override
    public int updateThankRedis() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.updateThankRedis();
    }

    @Override
    public List<TbPanelContent> getNavList() {
        ContentService m_pContentService=pPluginManager.FindModule(ContentService.class);
        return m_pContentService.getNavList();
    }
}
