package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.front.Member;
import com.noahframe.plugins.logic.mall.database.iface.LoginService;
import org.springframework.stereotype.Service;

/**
 * @Author:zoocee
 * @Date:2019/11/13 10:06
 */
@Service
public class LoginServiceImpl extends NFIBaseService implements LoginService {
    @Override
    public Member userLogin(String username, String password) {
        LoginService m_pLoginService=pPluginManager.FindModule(LoginService.class);
        return m_pLoginService.userLogin(username, password);
    }

    @Override
    public Member getUserByToken(String token) {
        LoginService m_pLoginService=pPluginManager.FindModule(LoginService.class);
        return m_pLoginService.getUserByToken(token);
    }

    @Override
    public int logout(String token) {
        LoginService m_pLoginService=pPluginManager.FindModule(LoginService.class);
        return m_pLoginService.logout(token);
    }
}
