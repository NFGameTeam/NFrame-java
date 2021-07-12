package cn.yeegro.nframe.plugin.oauthcenter.logic.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public abstract class NFISmsCodeAuthenticationToken extends AbstractAuthenticationToken {
    public NFISmsCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }
}
