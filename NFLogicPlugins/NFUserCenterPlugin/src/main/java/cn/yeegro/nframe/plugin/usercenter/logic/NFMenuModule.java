package cn.yeegro.nframe.plugin.usercenter.logic;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.common.utils.StringUtils;
import cn.yeegro.nframe.components.database.exception.EntityExistException;
import cn.yeegro.nframe.components.database.util.QueryHelp;
import cn.yeegro.nframe.components.database.util.ValidationUtil;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.MenuQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.MenuDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.RoleSmallDto;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.MenuMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Menu;
import cn.yeegro.nframe.plugin.usercenter.logic.model.User;
import cn.yeegro.nframe.plugin.usercenter.logic.model.vo.MenuMetaVo;
import cn.yeegro.nframe.plugin.usercenter.logic.model.vo.MenuVo;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIMenuModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIRBACModule;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.MenuRepository;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.UserRepository;
import org.pf4j.Extension;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

@Extension
public class NFMenuModule implements NFIMenuModule {


    private NFIPluginManager pPluginManager;
    private NFIRedisModule m_pRedisModule;
    private NFIRBACModule m_pRBACModule;

    private MenuRepository menuRepository;
    private UserRepository userRepository;
    private MenuMapper menuMapper;

    private static NFMenuModule SingletonPtr = null;

    public static NFMenuModule GetSingletonPtr() {
        if (null == SingletonPtr) {
            SingletonPtr = new NFMenuModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public List<MenuDto> queryAll(MenuQueryCriteria criteria, Boolean isQuery) throws Exception {

        menuMapper = SpringUtils.getBean(MenuMapper.class);
        menuRepository = SpringUtils.getBean(MenuRepository.class);
        Sort sort = Sort.by(Sort.Direction.ASC, "menuSort");
        if (isQuery) {
            criteria.setPidIsNull(true);
            List<Field> fields = QueryHelp.getAllFields(criteria.getClass(), new ArrayList<>());
            for (Field field : fields) {
                //设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                Object val = field.get(criteria);
                if ("pidIsNull".equals(field.getName())) {
                    continue;
                }
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        return menuMapper.toDto(menuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), sort));
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public MenuDto findById(long id) {
        Menu menu = menuRepository.findById(id).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(), "Menu", "id", id);
        return menuMapper.toDto(menu);
    }

    /**
     * 用户角色改变时需清理缓存
     *
     * @param currentUserId /
     * @return /
     */
    @Override
    //@Cacheable(key = "'user:' + #p0")
    public List<MenuDto> findByUser(Long currentUserId) {
        menuRepository = SpringUtils.getBean(MenuRepository.class);
        menuMapper = SpringUtils.getBean(MenuMapper.class);
        m_pRBACModule = pPluginManager.FindModule(NFIRBACModule.class);
        List<RoleSmallDto> roles = m_pRBACModule.findByUsersId(currentUserId);
        Set<Long> roleIds = roles.stream().map(RoleSmallDto::getId).collect(Collectors.toSet());
        LinkedHashSet<Menu> menus = new LinkedHashSet<>();
        //如果是admin用户
        if (roleIds.contains(1L)) {
            menus = menuRepository.findByAdmin(2);
        } else {
            menus = menuRepository.findByRoleIdsAndTypeNot(roleIds, 2);
        }
        return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Menu resources) {
        if (menuRepository.findByTitle(resources.getTitle()) != null) {
            throw new EntityExistException(Menu.class, "title", resources.getTitle());
        }
        if (StringUtils.isNotBlank(resources.getComponentName())) {
            if (menuRepository.findByComponentName(resources.getComponentName()) != null) {
                throw new EntityExistException(Menu.class, "componentName", resources.getComponentName());
            }
        }
        if (resources.getPid().equals(0L)) {
            resources.setPid(null);
        }
        if (resources.getIFrame()) {
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        menuRepository.save(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        if (resources.getPid() != null) {
            // 清理缓存
            updateSubCnt(resources.getPid());
        }

        RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);

        redisTemplate.delete("menu::pid:" + (resources.getPid() == null ? 0 : resources.getPid()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Menu resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Menu menu = menuRepository.findById(resources.getId()).orElseGet(Menu::new);
        // 记录旧的父节点ID
        Long pid = menu.getPid();
        ValidationUtil.isNull(menu.getId(), "Permission", "id", resources.getId());

        if (resources.getIFrame()) {
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        Menu menu1 = menuRepository.findByTitle(resources.getTitle());

        if (menu1 != null && !menu1.getId().equals(menu.getId())) {
            throw new EntityExistException(Menu.class, "title", resources.getTitle());
        }

        if (resources.getPid().equals(0L)) {
            resources.setPid(null);
        }
        if (StringUtils.isNotBlank(resources.getComponentName())) {
            menu1 = menuRepository.findByComponentName(resources.getComponentName());
            if (menu1 != null && !menu1.getId().equals(menu.getId())) {
                throw new EntityExistException(Menu.class, "componentName", resources.getComponentName());
            }
        }
        menu.setTitle(resources.getTitle());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setMenuSort(resources.getMenuSort());
        menu.setCache(resources.getCache());
        menu.setHidden(resources.getHidden());
        menu.setComponentName(resources.getComponentName());
        menu.setPermission(resources.getPermission());
        menu.setType(resources.getType());
        menuRepository.save(menu);
        // 计算子节点数目
        if (resources.getPid() == null) {
            updateSubCnt(pid);
        } else {
            pid = resources.getPid();
            updateSubCnt(resources.getPid());
        }
        // 清理缓存
        delCaches(resources.getId(), pid);
    }

    @Override
    public Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet) {
        // 递归找出待删除的菜单
        for (Menu menu1 : menuList) {
            menuSet.add(menu1);
            List<Menu> menus = menuRepository.findByPid(menu1.getId());
            if (menus != null && menus.size() != 0) {
                getDeleteMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Menu> menuSet) {
        m_pRBACModule = pPluginManager.FindModule(NFIRBACModule.class);
        for (Menu menu : menuSet) {
            // 清理缓存
            delCaches(menu.getId(), menu.getPid());
            m_pRBACModule.untiedMenu(menu.getId());
            menuRepository.deleteById(menu.getId());
            if (menu.getPid() != null) {
                updateSubCnt(menu.getPid());
            }
        }
    }

    @Override
    @Cacheable(key = "'pid:' + #p0")
    public List<MenuDto> getMenus(Long pid) {
        List<Menu> menus;
        if (pid != null && !pid.equals(0L)) {
            menus = menuRepository.findByPid(pid);
        } else {
            menus = menuRepository.findByPidIsNull();
        }
        return menuMapper.toDto(menus);
    }

    @Override
    public List<MenuDto> getSuperior(MenuDto menuDto, List<Menu> menus) {
        if (menuDto.getPid() == null) {
            menus.addAll(menuRepository.findByPidIsNull());
            return menuMapper.toDto(menus);
        }
        menus.addAll(menuRepository.findByPid(menuDto.getPid()));
        return getSuperior(findById(menuDto.getPid()), menus);
    }

    @Override
    public List<MenuDto> buildTree(List<MenuDto> menuDtos) {
        List<MenuDto> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDto menuDTO : menuDtos) {
            if (menuDTO.getPid() == null) {
                trees.add(menuDTO);
            }
            for (MenuDto it : menuDtos) {
                if (menuDTO.getId().equals(it.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuDto> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        List<MenuDto> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName()) ? menuDTO.getComponentName() : menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() : menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if (!menuDTO.getIFrame()) {
                            if (menuDTO.getPid() == null) {
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                            } else if (!StrUtil.isEmpty(menuDTO.getComponent())) {
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(), menuDTO.getIcon(), !menuDTO.getCache()));
                        if (menuDtoList != null && menuDtoList.size() != 0) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menuDTO.getPid() == null) {
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (!menuDTO.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    @Override
    public Menu findOne(Long id) {
        Menu menu = menuRepository.findById(id).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(), "Menu", "id", id);
        return menu;
    }

//    @Override
//    public void download(List<MenuDto> menuDtos, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (MenuDto menuDTO : menuDtos) {
//            Map<String,Object> map = new LinkedHashMap<>();
//            map.put("菜单标题", menuDTO.getTitle());
//            map.put("菜单类型", menuDTO.getType() == null ? "目录" : menuDTO.getType() == 1 ? "菜单" : "按钮");
//            map.put("权限标识", menuDTO.getPermission());
//            map.put("外链菜单", menuDTO.getIFrame() ? "是" : "否");
//            map.put("菜单可见", menuDTO.getHidden() ? "否" : "是");
//            map.put("是否缓存", menuDTO.getCache() ? "是" : "否");
//            map.put("创建日期", menuDTO.getCreateTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
//    }

    private void updateSubCnt(Long menuId) {
        int count = menuRepository.countByPid(menuId);
        menuRepository.updateSubCntById(count, menuId);
    }

    /**
     * 清理缓存
     *
     * @param id  菜单ID
     * @param pid 菜单父级ID
     */
    public void delCaches(Long id, Long pid) {
        RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);
        List<User> users = userRepository.findByMenuId(id);
        m_pRedisModule.del("menu::id:" + id);
        m_pRedisModule.delByKeys("menu::user:", users.stream().map(User::getId).collect(Collectors.toSet()));
        m_pRedisModule.del("menu::pid:" + (pid == null ? 0 : pid));
    }

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
        return false;
    }

    @Override
    public boolean AfterInit() {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRedisModule = pPluginManager.FindModule(NFIRedisModule.class);
        return true;
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
