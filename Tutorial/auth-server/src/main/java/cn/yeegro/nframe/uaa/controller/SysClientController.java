package cn.yeegro.nframe.uaa.controller;

import java.util.List;
import java.util.Map;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIClientModule;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import cn.yeegro.nframe.common.exception.controller.ControllerException;
import cn.yeegro.nframe.plugin.oauthcenter.logic.model.SysClient;
import cn.yeegro.nframe.common.web.PageResult;
import cn.yeegro.nframe.common.web.Result;
import cn.yeegro.nframe.log.annotation.LogAnnotation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 角色相关接口
 *
 * @author 刘斌（改）
 */
@RestController
@Api(tags = "CLIENT API")
@RequestMapping("/clients")
@SuppressWarnings("all")
public class SysClientController {


    private NFIPluginManager pPluginManager;
    private NFIClientModule m_pClientServiceModule;


    @GetMapping
    @ApiOperation(value = "应用列表")
    @PreAuthorize("hasAuthority('client:get/clients')")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public PageResult<SysClient> list(@RequestParam Map<String, Object> params) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pClientServiceModule = pPluginManager.FindModule(NFIClientModule.class);
        try {
            return m_pClientServiceModule.list(params);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取应用")
    @PreAuthorize("hasAuthority('client:get/clients/{id}')")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public SysClient get(@PathVariable Long id) {
        try {
            return m_pClientServiceModule.getById(id);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping("/all")
    @ApiOperation(value = "所有应用")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    @PreAuthorize("hasAnyAuthority('client:get/clients')")
    public List<SysClient> findList() {
        try {
            return m_pClientServiceModule.findList(Maps.newHashMap());
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除应用")
    @PreAuthorize("hasAuthority('client:delete/clients')")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public void delete(@PathVariable Long id) {
        try {
            m_pClientServiceModule.delete(id);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存或者修改应用")
    @PreAuthorize("hasAuthority('client:post/clients')")
    public Result saveOrUpdate(@RequestBody SysClient sysClient) {
        try {
            return m_pClientServiceModule.saveOrUpdate(sysClient);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @PutMapping("/updateEnabled")
    @ApiOperation(value = "修改状态")
    @PreAuthorize("hasAuthority('client:post/clients')")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public Result updateEnabled(@RequestBody Map<String, Object> params) {
        try {
            return m_pClientServiceModule.updateEnabled(params);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

}
