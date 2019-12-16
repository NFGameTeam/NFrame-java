package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.datas.OrderChartData;
import com.noahframe.plugins.logic.mall.database.iface.CountService;

import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

/**
 * @author Exrickx
 */
@Service
public class CountServiceImpl extends NFIBaseService implements CountService {


    @Override
    public List<OrderChartData> getOrderCountData(int type, Date startTime, Date endTime, int year) {
        CountService m_pCountService=pPluginManager.FindModule(CountService.class);
        return m_pCountService.getOrderCountData(type, startTime, endTime, year);
    }
}
