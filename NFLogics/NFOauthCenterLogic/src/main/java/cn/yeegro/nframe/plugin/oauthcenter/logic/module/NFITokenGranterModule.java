package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.functor.AUTHOR_CONFIG_EVENT;
import cn.yeegro.nframe.plugin.oauthcenter.logic.service.NFIValidateCodeService;
import org.springframework.security.oauth2.provider.TokenGranter;

import java.util.ArrayList;
import java.util.List;

public interface NFITokenGranterModule extends NFIModule {

    List<AUTHOR_CONFIG_EVENT> mx_ConfigEvents=new ArrayList();;

    List<TokenGranter> mx_TokenGranterList=new ArrayList<>();

    List<TokenGranter> getTokenGranterList();

    NFIValidateCodeService getValidateCodeService();
}
