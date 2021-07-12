package cn.yeegro.nframe.plugin.usercenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.common.utils.PageUtil;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.common.utils.StringUtils;
import cn.yeegro.nframe.components.database.exception.EntityExistException;
import cn.yeegro.nframe.components.database.util.QueryHelp;
import cn.yeegro.nframe.components.database.util.ValidationUtil;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFITokenStoreConfigerModule;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.SysPermissionQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.SysRoleQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.SysRolesPermsQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.*;
import cn.yeegro.nframe.plugin.usercenter.logic.enums.DataScopeEnum;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.RoleSmallMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.SysRoleMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.model.*;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIDeptModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIRBACModule;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.SysRoleRepository;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.UserRepository;
import org.pf4j.Extension;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Extension
public class NFRBACModule implements NFIRBACModule {

    private NFIPluginManager pPluginManager;
    private NFIRedisModule m_pRedisModule;
    private NFIDeptModule m_pDeptModule;

    private SysRoleRepository roleRepository;
    private SysRoleMapper roleMapper;
    private RoleSmallMapper roleSmallMapper;
    private UserRepository userRepository;

    private static NFRBACModule SingletonPtr=null;

    public static NFRBACModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFRBACModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public List<SysRoleDto> queryAll() {
        roleMapper= SpringUtils.getBean(SysRoleMapper.class);
        Sort sort = Sort.by(Sort.Direction.ASC, "level");
        return roleMapper.toDto(roleRepository.findAll(sort));
    }

    @Override
    public List<SysRoleDto> queryAll(SysRoleQueryCriteria criteria) {
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        roleMapper= SpringUtils.getBean(SysRoleMapper.class);
        return roleMapper.toDto(roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public Object queryAll(SysRoleQueryCriteria criteria, Pageable pageable) {
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        roleMapper= SpringUtils.getBean(SysRoleMapper.class);
        Page<SysRole> page = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    @Transactional
    @Cacheable(key = "'id:' + #p0")
    public SysRoleDto RoleFindById(long id) {
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        roleMapper= SpringUtils.getBean(SysRoleMapper.class);
        SysRole role = roleRepository.findById(id).orElseGet(SysRole::new);
        ValidationUtil.isNull(role.getId(),"Role","id",id);
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SysRole resources) {
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        if(roleRepository.findByName(resources.getName()) != null){
            throw new EntityExistException(SysRole.class,"username",resources.getName());
        }
        roleRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRole resources) {
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        SysRole role = roleRepository.findById(resources.getId()).orElseGet(SysRole::new);
        ValidationUtil.isNull(role.getId(),"Role","id",resources.getId());

        SysRole role1 = roleRepository.findByName(resources.getName());

        if(role1 != null && !role1.getId().equals(role.getId())){
            throw new EntityExistException(SysRole.class,"username",resources.getName());
        }
        role.setName(resources.getName());
        role.setDescription(resources.getDescription());
        role.setDataScope(resources.getDataScope());
        role.setDepts(resources.getDepts());
        role.setLevel(resources.getLevel());
        roleRepository.save(role);
        // 更新相关缓存
        delCaches(role.getId());
    }

    @Override
    public void updateMenu(SysRole resources, SysRoleDto roleDTO) {
        userRepository=SpringUtils.getBean(UserRepository.class);
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        roleMapper= SpringUtils.getBean(SysRoleMapper.class);
        SysRole role = roleMapper.toEntity(roleDTO);
        // 清理缓存
        List<User> users = userRepository.findByRoleId(role.getId());
        Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
        m_pRedisModule.delByKeys("menu::user:",userIds);
        // 更新菜单
        role.setMenus(resources.getMenus());
        roleRepository.save(role);
    }

    @Override
    public void updatePermission(SysRole resources, SysRoleDto roleDTO) {

        userRepository=SpringUtils.getBean(UserRepository.class);
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        roleMapper= SpringUtils.getBean(SysRoleMapper.class);

        SysRole role = roleMapper.toEntity(roleDTO);
        // 清理缓存
        List<User> users = userRepository.findByRoleId(role.getId());
        Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
        m_pRedisModule.delByKeys("menu::user:",userIds);

        role.setPermissions(resources.getPermissions());
        roleRepository.save(role);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Long menuId) {
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        // 更新菜单
        roleRepository.untiedMenu(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoles(Set<Long> ids) {
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        for (Long id : ids) {
            // 更新相关缓存
            delCaches(id);
        }
        roleRepository.deleteAllByIdIn(ids);
    }

    @Override
    public List<RoleSmallDto> findByUsersId(Long id) {
        roleSmallMapper=SpringUtils.getBean(RoleSmallMapper.class);
        roleRepository=SpringUtils.getBean(SysRoleRepository.class);
        return roleSmallMapper.toDto(new ArrayList<>(roleRepository.findByUserId(id)));
    }

    @Override
    public Integer findByRoles(Set<SysRole> roles) {
        Set<SysRoleDto> roleDtos = new HashSet<>();
        for (SysRole role : roles) {
            roleDtos.add(RoleFindById(role.getId()));
        }
        return Collections.min(roleDtos.stream().map(SysRoleDto::getLevel).collect(Collectors.toList()));
    }

    @Override
    // @Cacheable(key = "'auth:' + #p0.id")
    public List<GrantedAuthority> mapToGrantedAuthorities(UserDto user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if(user.getIsAdmin()){
            permissions.add("admin");

            List<GrantedAuthority> grantedAuthorities=permissions.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return grantedAuthorities;
        }
        Set<SysRole> roles = roleRepository.findByUserId(user.getId());


        permissions =roles.stream().flatMap(role -> role.getPermissions().stream())
                .filter(sysPermission -> StringUtils.isNotBlank( sysPermission.getCode()))
                .map(SysPermission::getCode).collect(Collectors.toSet());

        List<SysPermissionDto> auths=queryUserID(user.getId());
        for (SysPermissionDto permissionDto:auths)
        {
            permissions.add(permissionDto.getCode());
        }

        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public void downloadSysRole(List<SysRoleDto> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysRoleDto role : roles) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
//        FileUtil.downloadExcel(list, response);
    }

    /**
     * 清理缓存
     * @param id /
     */
    public void delCaches(Long id){
        userRepository=SpringUtils.getBean(UserRepository.class);
        List<User> users = userRepository.findByRoleId(id);
        Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
        m_pRedisModule.delByKeys("data::user:",userIds);
        m_pRedisModule.delByKeys("menu::user:",userIds);
        m_pRedisModule.delByKeys("role::auth:",userIds);
    }

    @Override
    public void verification(Set<Long> ids) {
        if(userRepository.countByRoles(ids) > 0){
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }







    @Override
    public Map<String, Object> queryAll(SysPermissionQueryCriteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public List<SysPermissionDto> queryAll(SysPermissionQueryCriteria criteria) {
        return null;
    }

    @Override
    public List<SysPermissionDto> queryByRoleId(long roleId) {
        return null;
    }

    @Override
    public List<SysPermissionDto> queryUserID(Long userid) {
        return null;
    }

    @Override
    public SysPermissionDto permissionFindById(Long permId) {
        return null;
    }

    @Override
    public SysPermissionDto create(SysPermission resources) {
        return null;
    }

    @Override
    public void update(SysPermission resources) {

    }

    @Override
    public void deleteAllPermission(Long[] ids) {

    }

    @Override
    public void downloadPermission(List<SysPermissionDto> all, HttpServletResponse response) throws IOException {

    }

    @Override
    public Map<String, Object> queryAll(SysRolesPermsQueryCriteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public List<SysRolesPermsDto> queryAll(SysRolesPermsQueryCriteria criteria) {
        return null;
    }

    @Override
    public SysRolesPermsDto RolesPermsFindById(Long permId) {
        return null;
    }

    @Override
    public SysRolesPermsDto findByRoleId(Long roleID) {
        return null;
    }

    @Override
    public SysRolesPermsDto create(SysRolesPerms resources) {
        return null;
    }

    @Override
    public void update(SysRolesPerms resources) {

    }

    @Override
    public void deleteAllRolesPerm(Long[] ids) {

    }

    @Override
    public void downloadRolesPerm(List<SysRolesPermsDto> all, HttpServletResponse response) throws IOException {

    }

    /**
     * 用户角色改变时需清理缓存
     * @param user /
     * @return /
     */
    @Override
    @Cacheable(key = "'user:' + #p0.id")
    public List<Long> getDeptIds(UserDto user) {
        // 用于存储部门id
        Set<Long> deptIds = new HashSet<>();
        // 查询用户角色
        List<RoleSmallDto> roleSet = findByUsersId(user.getId());
        // 获取对应的部门ID
        for (RoleSmallDto role : roleSet) {
            DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case THIS_LEVEL:
                    deptIds.add(user.getDept().getId());
                    break;
                case CUSTOMIZE:
                    deptIds.addAll(getCustomize(deptIds, role));
                    break;
                default:
                    break;
            }
        }
        return new ArrayList<>(deptIds);
    }

    /**
     * 获取自定义的数据权限
     * @param deptIds 部门ID
     * @param role 角色
     * @return 数据权限ID
     */
    public Set<Long> getCustomize(Set<Long> deptIds, RoleSmallDto role){
        Set<Dept> depts = m_pDeptModule.findByRoleId(role.getId());
        for (Dept dept : depts) {
            deptIds.add(dept.getId());
            List<Dept> deptChildren = m_pDeptModule.findByPid(dept.getId());
            if (deptChildren != null && deptChildren.size() != 0) {
                deptIds.addAll(m_pDeptModule.getDeptChildren(dept.getId(), deptChildren));
            }
        }
        return deptIds;
    }

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRedisModule = pPluginManager.FindModule(NFIRedisModule.class);
        return true;
    }

    @Override
    public boolean AfterInit() {
        return false;
    }

    @Override
    public boolean CheckConfig() {
        return false;
    }

    @Override
    public boolean ReadyExecute() {
        return false;
    }

    @Override
    public boolean Execute() {
        return false;
    }

    @Override
    public boolean BeforeShut() {
        return false;
    }

    @Override
    public boolean Shut() {
        return false;
    }

    @Override
    public boolean Finalize() {
        return false;
    }

    @Override
    public boolean OnReloadPlugin() {
        return false;
    }
}
