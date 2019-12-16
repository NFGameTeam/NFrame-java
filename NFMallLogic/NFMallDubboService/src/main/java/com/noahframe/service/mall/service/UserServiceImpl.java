package com.noahframe.service.mall.service;

import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.model.TbPermission;
import com.noahframe.plugins.logic.mall.database.model.TbRole;
import com.noahframe.plugins.logic.mall.database.model.TbUser;
import com.noahframe.plugins.logic.mall.datas.DataTablesResult;
import com.noahframe.plugins.logic.mall.database.iface.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Exrickx
 */
@Service
public class UserServiceImpl extends NFIBaseService implements UserService {


    @Override
    public TbUser getUserByUsername(String username) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserByUsername(username);
    }

    @Override
    public Set<String> getRoles(String username) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getRoles(username);
    }

    @Override
    public Set<String> getPermissions(String username) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getPermissions(username);
    }

    @Override
    public DataTablesResult getRoleList() {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getRoleList();
    }

    @Override
    public List<TbRole> getAllRoles() {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getAllRoles();
    }

    @Override
    public int addRole(TbRole tbRole) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.addRole(tbRole);
    }

    @Override
    public TbRole getRoleByRoleName(String roleName) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getRoleByRoleName(roleName);
    }

    @Override
    public boolean getRoleByEditName(int id, String roleName) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getRoleByEditName(id, roleName);
    }

    @Override
    public int updateRole(TbRole tbRole) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.updateRole(tbRole);
    }

    @Override
    public int deleteRole(int id) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.deleteRole(id);
    }

    @Override
    public Long countRole() {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.countRole();
    }

    @Override
    public DataTablesResult getPermissionList() {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getPermissionList();
    }

    @Override
    public int addPermission(TbPermission tbPermission) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.addPermission(tbPermission);
    }

    @Override
    public int updatePermission(TbPermission tbPermission) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.updatePermission(tbPermission);
    }

    @Override
    public int deletePermission(int id) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.deletePermission(id);
    }

    @Override
    public Long countPermission() {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.countPermission();
    }

    @Override
    public DataTablesResult getUserList() {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserList();
    }

    @Override
    public int addUser(TbUser user) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.addUser(user);
    }

    @Override
    public TbUser getUserById(Long id) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserById(id);
    }

    @Override
    public boolean getUserByName(String username) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserByName(username);
    }

    @Override
    public boolean getUserByPhone(String phone) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserByPhone(phone);
    }

    @Override
    public boolean getUserByEmail(String emaill) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserByEmail(emaill);
    }

    @Override
    public int updateUser(TbUser user) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.updateUser(user);
    }

    @Override
    public int changeUserState(Long id, int state) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.changeUserState(id, state);
    }

    @Override
    public int changePassword(TbUser tbUser) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.changePassword(tbUser);
    }

    @Override
    public boolean getUserByEditName(Long id, String username) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserByEditName(id, username);
    }

    @Override
    public boolean getUserByEditPhone(Long id, String phone) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserByEditPhone(id, phone);
    }

    @Override
    public boolean getUserByEditEmail(Long id, String emaill) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.getUserByEditEmail(id, emaill);
    }

    @Override
    public int deleteUser(Long userId) {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.deleteUser(userId);
    }

    @Override
    public Long countUser() {
        UserService m_pUserService=pPluginManager.FindModule(UserService.class);
        return m_pUserService.countUser();
    }
}
