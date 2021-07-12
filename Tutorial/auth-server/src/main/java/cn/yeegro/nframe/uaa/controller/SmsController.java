package cn.yeegro.nframe.uaa.controller;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenGranterModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.service.NFIValidateCodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.yeegro.nframe.common.utils.StringUtil;
import cn.yeegro.nframe.common.web.Result;
import cn.yeegro.nframe.log.annotation.LogAnnotation;

/**
 * 短信提供
 *
 * @author zzg
 * @date 2019/09/01
 */
@RestController
@SuppressWarnings("all")
public class SmsController {

    public final static String SYSMSG_LOGIN_PWD_MSG = "验证码：{0}，3分钟内有效";
    private NFIPluginManager pPluginManager;

    @RequestMapping("/sms/send")
    @LogAnnotation(module = "auth-server", recordRequestParam = false)
    public Result sendSms(@RequestParam(value = "mobile", required = false) String mobile) {
        String content = SmsController.SYSMSG_LOGIN_PWD_MSG.replace("{0}", StringUtil.generateRamdomNum());
//        SendMsgResult sendMsgResult = MobileMsgConfig.sendMsg(mobile, content);

        String calidateCode = StringUtil.generateRamdomNum();

        // TODO: 2019-08-29 发送短信验证码 每个公司对接不同，自己实现
        pPluginManager = NFPluginManager.GetSingletonPtr();
        NFITokenGranterModule m_pTokenGranterModule = pPluginManager.FindModule(NFITokenGranterModule.class);
        NFIValidateCodeService validateCodeService = m_pTokenGranterModule.getValidateCodeService();
        validateCodeService.saveImageCode(mobile, calidateCode);
        return Result.succeed(calidateCode, "发送成功");
    }

}
