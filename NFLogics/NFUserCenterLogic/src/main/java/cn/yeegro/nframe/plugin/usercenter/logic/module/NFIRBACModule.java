package cn.yeegro.nframe.plugin.usercenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.SysPermissionQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.SysRoleQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.SysRolesPermsQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.*;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysPermission;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysRole;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysRolesPerms;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NFIRBACModule extends NFIModule {

    /**
     * 查询全部数据
     * @return /
     */
    List<SysRoleDto> queryAll();

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    SysRoleDto RoleFindById(long id);

    /**
     * 创建
     * @param resources /
     */
    void create(SysRole resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(SysRole resources);

    /**
     * 删除
     * @param ids /
     */
    void deleteRoles(Set<Long> ids);

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    List<RoleSmallDto> findByUsersId(Long id);

    /**
     * 根据角色查询角色级别
     * @param roles /
     * @return /
     */
    Integer findByRoles(Set<SysRole> roles);

    /**
     * 修改绑定的菜单
     * @param resources /
     * @param roleDTO /
     */
    void updateMenu(SysRole resources, SysRoleDto roleDTO);

    /**
     * 修改绑定的权限
     * @param resources /
     * @param roleDTO /
     */
    void updatePermission(SysRole resources, SysRoleDto roleDTO);

    /**
     * 解绑菜单
     * @param id /
     */
    void untiedMenu(Long id);

    /**
     * 待条件分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(SysRoleQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    List<SysRoleDto> queryAll(SysRoleQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadSysRole(List<SysRoleDto> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(UserDto user);

    /**
     * 验证是否被用户关联
     * @param ids /
     */
    void verification(Set<Long> ids);


    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    Map<String,Object> queryAll(SysPermissionQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<SysPermissionDto>
     */
    List<SysPermissionDto> queryAll(SysPermissionQueryCriteria criteria);


    List<SysPermissionDto> queryByRoleId(long roleId);

    /**
     * 查询所有用户的临时权限
     * @param userid 条件参数
     * @return List<SysAuthsDto>
     */
    List<SysPermissionDto> queryUserID(Long userid);

    /**
     * 根据ID查询
     * @param permId ID
     * @return SysPermissionDto
     */
    SysPermissionDto permissionFindById(Long permId);

    /**
     * 创建
     * @param resources /
     * @return SysPermissionDto
     */
    SysPermissionDto create(SysPermission resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(SysPermission resources);

    /**
     * 多选删除
     * @param ids /
     */
    void deleteAllPermission(Long[] ids);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadPermission(List<SysPermissionDto> all, HttpServletResponse response) throws IOException;


    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    Map<String,Object> queryAll(SysRolesPermsQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<SysRolesPermsDto>
     */
    List<SysRolesPermsDto> queryAll(SysRolesPermsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param permId ID
     * @return SysRolesPermsDto
     */
    SysRolesPermsDto RolesPermsFindById(Long permId);

    /**
     * 根据RoleID查询
     * @param roleID ID
     * @return SysRolesPermsDto
     */
    SysRolesPermsDto findByRoleId(Long roleID);

    /**
     * 创建
     * @param resources /
     * @return SysRolesPermsDto
     */
    SysRolesPermsDto create(SysRolesPerms resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(SysRolesPerms resources);

    /**
     * 多选删除
     * @param ids /
     */
    void deleteAllRolesPerm(Long[] ids);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadRolesPerm(List<SysRolesPermsDto> all, HttpServletResponse response) throws IOException;


    /**
     * 获取数据权限
     * @param user /
     * @return /
     */
    List<Long> getDeptIds(UserDto user);

}
