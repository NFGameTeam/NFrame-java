package cn.yeegro.nframe.log.dto;

import java.util.Optional;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.util.TokenUtil;
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import com.github.structlog4j.IToLog;
import cn.yeegro.nframe.log.util.TraceUtil;

import lombok.Builder;
import lombok.Data;

/**
 * 业务日志
 * @author someday
 * @create 2020年04月02日


 */
@Data
@Builder
public class LogEntry implements IToLog {
   
    private String transId;
    private String path ;
    private String clazz ;
    private String method ;
    private String token ;
    private String username ;
    private String msg ;
    private String error ;


    private NFIPluginManager pPluginManager;
    private NFIUserModule m_pSysUserModule;

    @Override
    public Object[] toLog() {
        pPluginManager= NFPluginManager.GetSingletonPtr();
        m_pSysUserModule=pPluginManager.FindModule(NFIUserModule.class);
        return new Object[] {
                "transId",  Optional.ofNullable(TraceUtil.getTrace()).orElse(""),
                "path",Optional.ofNullable(path).orElse(""),
                "clazz",Optional.ofNullable(clazz).orElse(""),
                "method",Optional.ofNullable(method).orElse(""),
                "token" , Optional.ofNullable(TokenUtil.getToken()).orElse("") ,
                "username", Optional.ofNullable(m_pSysUserModule.getLoginAppUser()).orElse(new LoginAppUser()).getUsername(),
                "msg" , Optional.ofNullable(msg).orElse(""),
                "error",Optional.ofNullable(error).orElse("")
                
        };
    }
}