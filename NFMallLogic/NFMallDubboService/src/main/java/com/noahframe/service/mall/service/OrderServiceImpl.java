package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.front.Order;
import com.noahframe.plugins.logic.mall.database.front.OrderInfo;
import com.noahframe.plugins.logic.mall.database.front.PageOrder;
import com.noahframe.plugins.logic.mall.database.model.TbThanks;
import com.noahframe.plugins.logic.mall.datas.DataTablesResult;
import com.noahframe.plugins.logic.mall.database.pojo.OrderDetail;
import com.noahframe.plugins.logic.mall.database.iface.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * @author Exrickx
 */
@Service
public class OrderServiceImpl extends NFIBaseService implements OrderService {


    @Override
    public PageOrder getOrderList(Long userId, int page, int size) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.getOrderList(userId, page, size);
    }

    @Override
    public Order getOrder(Long orderId) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.getOrder(orderId);
    }

    @Override
    public int cancelOrder(Long orderId) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.cancelOrder(orderId);
    }

    @Override
    public Long createOrder(OrderInfo orderInfo) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.createOrder(orderInfo);
    }

    @Override
    public int delOrder(Long orderId) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.delOrder(orderId);
    }

    @Override
    public int payOrder(TbThanks tbThanks) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.payOrder(tbThanks);
    }

    @Override
    public DataTablesResult getOrderList(int draw, int start, int length, String search, String orderCol, String orderDir) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.getOrderList(draw, start, length, search, orderCol, orderDir);
    }

    @Override
    public Long countOrder() {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.countOrder();
    }

    @Override
    public OrderDetail getOrderDetail(String orderId) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.getOrderDetail(orderId);
    }

    @Override
    public int deliver(String orderId, String shippingName, String shippingCode, BigDecimal postFee) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.deliver(orderId, shippingName, shippingCode, postFee);
    }

    @Override
    public int remark(String orderId, String message) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.remark(orderId, message);
    }

    @Override
    public int cancelOrderByAdmin(String orderId) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return 0;
    }

    @Override
    public int deleteOrder(String id) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.deleteOrder(id);
    }

    @Override
    public int cancelOrder() {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.cancelOrder();
    }

    @Override
    public int passPay(String tokenName, String token, String id) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.passPay(tokenName, token, id);
    }

    @Override
    public int backPay(String tokenName, String token, String id) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return 0;
    }

    @Override
    public int notShowPay(String tokenName, String token, String id) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.notShowPay(tokenName, token, id);
    }

    @Override
    public int editPay(String tokenName, String token, TbThanks tbThanks) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.editPay(tokenName, token, tbThanks);
    }

    @Override
    public int payDelNotNotify(String tokenName, String token, String id) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.payDelNotNotify(tokenName, token, id);
    }

    @Override
    public int payDel(String tokenName, String token, String id) {
        OrderService m_pOrderService=pPluginManager.FindModule(OrderService.class);
        return m_pOrderService.payDel(tokenName, token, id);
    }
}
