package cn.yeegro.nframe.plugin.oauthcenter.logic.service;


import javax.servlet.http.HttpServletRequest;

/**
 * @author zlt
 * @date 2018/12/10
 */
public interface NFIValidateCodeService {
    /**
     * 保存图形验证码
     * @param deviceId 前端唯一标识
     * @param imageCode 验证码
     */
    void saveImageCode(String deviceId, String imageCode);

    /**
     * 获取验证码
     * @param deviceId 前端唯一标识/手机号
     */
    String getCode(String deviceId);

    /**
     * 删除验证码
     * @param deviceId 前端唯一标识/手机号
     */
    void remove(String deviceId);

    /**
     * 验证验证码
     */
    void validate(HttpServletRequest request);
    
    
    void validate(String deviceId, String validCode);
}
