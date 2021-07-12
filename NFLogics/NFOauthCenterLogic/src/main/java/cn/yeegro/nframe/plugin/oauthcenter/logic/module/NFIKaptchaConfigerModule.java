package cn.yeegro.nframe.plugin.oauthcenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import com.google.code.kaptcha.impl.DefaultKaptcha;

public interface NFIKaptchaConfigerModule extends NFIModule {

    DefaultKaptcha KaptchaConfiger();
}
