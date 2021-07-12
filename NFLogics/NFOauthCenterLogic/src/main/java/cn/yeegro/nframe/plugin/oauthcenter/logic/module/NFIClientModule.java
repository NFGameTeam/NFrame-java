package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.common.web.PageResult;
import cn.yeegro.nframe.common.web.Result;
import cn.yeegro.nframe.plugin.oauthcenter.logic.model.SysClient;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface NFIClientModule extends NFIModule {

    Map getClient(String clientId);
    @Cacheable(value = "service", key = "#clientId")
    List<Map> listByClientId(Long clientId);

    Result saveOrUpdate(SysClient clientDto);

    void delete(Long id);

    Result updateEnabled(Map<String, Object> params);

    SysClient getById(Long id) ;



    public PageResult<SysClient> list(Map<String, Object> params);

    List<SysClient> findList(Map<String, Object> params) ;
}
