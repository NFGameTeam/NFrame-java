package cn.yeegro.nframe.plugin.oauthcenter.logic.token;

import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

public abstract class NFIResJwtTokenStore extends JwtTokenStore {
    public NFIResJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
        super(jwtTokenEnhancer);
    }
}
