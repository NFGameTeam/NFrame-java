package cn.yeegro.nframe.plugin.oauthcenter.logic.auth.details;

import java.io.Serializable;

import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2017年11月12日 上午22:57:51


 * 类说明 客户端应用信息
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DefaultClientDetails extends BaseClientDetails implements Serializable {
    private static final long serialVersionUID = -4996423520248249518L;
    
    private long id ;
    //限流标识  
    private long ifLimit;
    //限流次数
    private long limitCount;


    public DefaultClientDetails(String clientId, String resourceIds, String scopes,
                                String grantTypes, String authorities, String redirectUris) {
        super(clientId, resourceIds, scopes, grantTypes, authorities, redirectUris);
    }


}
