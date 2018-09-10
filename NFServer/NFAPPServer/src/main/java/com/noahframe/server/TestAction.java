package com.noahframe.server;

import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.iface.NFIPluginManager;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.module.NFILogModule;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("prototype")
@RequestMapping(value = "/Test")
public class TestAction {

    private NFIPluginManager pPluginManager;

    @ResponseBody
    @RequestMapping(value = "log.html", method = RequestMethod.GET)
    public ModelAndView Log(ModelMap map) {

        pPluginManager = NFPluginManager.GetSingletonPtr();
        NFILogModule m_pLogModule=pPluginManager.FindModule(NFILogModule.class);
        m_pLogModule.LogObject(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "测试LogObject", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
        m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_ERROR_NORMAL, new NFGUID(), "测试LogNormal-NLL_ERROR_NORMAL", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
        m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_DEBUG_NORMAL, new NFGUID(), "测试LogNormal-NLL_DEBUG_NORMAL", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
        m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_INFO_NORMAL, new NFGUID(), "测试LogNormal-NLL_INFO_NORMAL", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
        m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_WARING_NORMAL, new NFGUID(), "测试LogNormal-NLL_WARING_NORMAL", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());
        m_pLogModule.LogNormal(NFILogModule.NF_LOG_LEVEL.NLL_FATAL_NORMAL, new NFGUID(), "测试LogNormal-NLL_FATAL_NORMAL", m_pLogModule.CurrTrace().getMethodName(), m_pLogModule.CurrTrace().getLineNumber());


        return new ModelAndView("test/log");
    }
}
