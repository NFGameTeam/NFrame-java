package cn.yeegro.nframe.plugin.usercenter.logic.auth.details;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.RoleSmallDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.UserDto;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysRole;
import cn.yeegro.nframe.plugin.usercenter.logic.model.User;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 用户实体绑定spring security


 */
@Data
public class LoginAppUser extends UserDto implements UserDetails {

    private static final long serialVersionUID = -3685249101751401211L;


    private Set<RoleSmallDto> sysRoles;

    private Set<String> permissions;



    /***
     * 权限重写
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        NFIPluginManager pPluginManager= NFPluginManager.GetSingletonPtr();
        NFIUserModule m_pSysUserModule=pPluginManager.FindModule(NFIUserModule.class);
       return m_pSysUserModule.getAuthorities(sysRoles,permissions);
    }


    @JsonIgnore
    public Collection<? extends GrantedAuthority> putAll(Collection<GrantedAuthority> collections) {
        NFIPluginManager pPluginManager= NFPluginManager.GetSingletonPtr();
        NFIUserModule m_pSysUserModule=pPluginManager.FindModule(NFIUserModule.class);
        return m_pSysUserModule.putAll(collections);
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

    /**
     * 获取当前用户的数据权限
     * @return /
     */
    public List<Long> getCurrentUserDataScope(){
        UserDetails userDetails = this;
        JSONArray array = JSONUtil.parseArray(new JSONObject(userDetails).get("dataScopes"));
        return JSONUtil.toList(array,Long.class);
    }

}
