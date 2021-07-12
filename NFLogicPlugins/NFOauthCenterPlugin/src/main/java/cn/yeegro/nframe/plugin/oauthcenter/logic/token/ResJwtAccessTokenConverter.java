package cn.yeegro.nframe.plugin.oauthcenter.logic.token;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.RoleSmallDto;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;


import cn.hutool.core.bean.BeanUtil;
@SuppressWarnings("all") 
public class ResJwtAccessTokenConverter extends NFIResJwtAccessTokenConverter{
	
	
	public ResJwtAccessTokenConverter() {
		super();
		super.setAccessTokenConverter(new JwtUserAuthenticationConverter());
	}
	public  class JwtUserAuthenticationConverter extends DefaultAccessTokenConverter {
		
		
		public JwtUserAuthenticationConverter (){
			super.setUserTokenConverter(  new JWTfaultUserAuthenticationConverter());
		}
		
		
		public  class JWTfaultUserAuthenticationConverter extends DefaultUserAuthenticationConverter{
			
			public Authentication extractAuthentication(Map<String, ?> map) {
				
				if (map.containsKey(USERNAME)) {
					Object principal = map.get(USERNAME);
//					Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
					LoginAppUser loginUser = new LoginAppUser();
					if (principal instanceof Map) {

						loginUser = BeanUtil.mapToBean((Map) principal, LoginAppUser.class, true);
						 
						Set<RoleSmallDto> roles = new HashSet<>();
						
						for(Iterator<RoleSmallDto> it = loginUser.getSysRoles().iterator(); it.hasNext();){
							RoleSmallDto role =  BeanUtil.mapToBean((Map) it.next() , RoleSmallDto.class, false);
							roles.add(role) ;
						}
						loginUser.setSysRoles(roles); 
					} 
					return new UsernamePasswordAuthenticationToken(loginUser, "N/A", loginUser.getAuthorities());
				}
				
				
				 
				return null;
			}
			 
		}
		
	}
}
