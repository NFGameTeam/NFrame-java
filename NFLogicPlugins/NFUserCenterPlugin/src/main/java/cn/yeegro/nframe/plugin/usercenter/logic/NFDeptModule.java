package cn.yeegro.nframe.plugin.usercenter.logic;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.database.util.QueryHelp;
import cn.yeegro.nframe.components.database.util.ValidationUtil;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.DeptQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.DeptDto;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.DeptMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Dept;
import cn.yeegro.nframe.plugin.usercenter.logic.model.User;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIDeptModule;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.DeptRepository;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.SysRoleRepository;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.UserRepository;
import org.pf4j.Extension;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Extension
public class NFDeptModule implements NFIDeptModule {

    private NFIPluginManager pPluginManager;
    private NFIRedisModule m_pRedisModule;
    private DeptRepository deptRepository;
    private DeptMapper deptMapper;

    private static NFDeptModule SingletonPtr=null;

    public static NFDeptModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFDeptModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public List<DeptDto> queryAll(DeptQueryCriteria criteria, Boolean isQuery) throws Exception {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        deptMapper= SpringUtils.getBean(DeptMapper.class);
        Sort sort = Sort.by(Sort.Direction.ASC, "deptSort");
        if (isQuery) {
            criteria.setPidIsNull(true);
            List<Field> fields = QueryHelp.getAllFields(criteria.getClass(), new ArrayList<>());
            List<String> fieldNames = new ArrayList<String>(){{ add("pidIsNull");add("enabled");}};
            for (Field field : fields) {
                //设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                Object val = field.get(criteria);
                if(fieldNames.contains(field.getName())){
                    continue;
                }
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        return deptMapper.toDto(deptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),sort));
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public DeptDto findById(Long id) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        deptMapper= SpringUtils.getBean(DeptMapper.class);
        Dept dept = deptRepository.findById(id).orElseGet(Dept::new);
        ValidationUtil.isNull(dept.getId(),"Dept","id",id);
        return deptMapper.toDto(dept);
    }

    @Override
    @Cacheable(key = "'pid:' + #p0")
    public List<Dept> findByPid(long pid) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        return deptRepository.findByPid(pid);
    }

    @Override
    public Set<Dept> findByRoleId(Long id) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        return deptRepository.findByRoleId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Dept resources) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        deptRepository.save(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        if(resources.getPid() != null){
            // 清理缓存
            m_pRedisModule.del("dept::pid:" + resources.getPid());
            updateSubCnt(resources.getPid());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dept resources) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        // 旧的部门
        Long pid = findById(resources.getId()).getPid();
        if(resources.getPid() != null && resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Dept dept = deptRepository.findById(resources.getId()).orElseGet(Dept::new);
        ValidationUtil.isNull( dept.getId(),"Dept","id",resources.getId());
        resources.setId(dept.getId());
        deptRepository.save(resources);
        if(resources.getPid() == null){
            updateSubCnt(pid);
        } else {
            pid = resources.getPid();
            updateSubCnt(resources.getPid());
        }
        // 清理缓存
        delCaches(resources.getId(), pid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<DeptDto> deptDtos) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        for (DeptDto deptDto : deptDtos) {
            // 清理缓存
            delCaches(deptDto.getId(), deptDto.getPid());
            deptRepository.deleteById(deptDto.getId());
            if(deptDto.getPid() != null){
                updateSubCnt(deptDto.getPid());
            }
        }
    }

    @Override
    public void download(List<DeptDto> deptDtos, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (DeptDto deptDTO : deptDtos) {
//            Map<String,Object> map = new LinkedHashMap<>();
//            map.put("部门名称", deptDTO.getName());
//            map.put("部门状态", deptDTO.getEnabled() ? "启用" : "停用");
//            map.put("创建日期", deptDTO.getCreateTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Set<DeptDto> getDeleteDepts(List<Dept> menuList, Set<DeptDto> deptDtos) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        deptMapper= SpringUtils.getBean(DeptMapper.class);
        for (Dept dept : menuList) {
            deptDtos.add(deptMapper.toDto(dept));
            List<Dept> depts = deptRepository.findByPid(dept.getId());
            if(depts!=null && depts.size()!=0){
                getDeleteDepts(depts, deptDtos);
            }
        }
        return deptDtos;
    }

    @Override
    public List<Long> getDeptChildren(Long deptId, List<Dept> deptList) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept!=null && dept.getEnabled()){
                        List<Dept> depts = deptRepository.findByPid(dept.getId());
                        if(deptList.size() != 0){
                            list.addAll(getDeptChildren(dept.getId(), depts));
                        }
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }

    @Override
    public List<DeptDto> getSuperior(DeptDto deptDto, List<Dept> depts) {
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        deptMapper= SpringUtils.getBean(DeptMapper.class);
        if(deptDto.getPid() == null){
            depts.addAll(deptRepository.findByPidIsNull());
            return deptMapper.toDto(depts);
        }
        depts.addAll(deptRepository.findByPid(deptDto.getPid()));
        return getSuperior(findById(deptDto.getPid()), depts);
    }

    @Override
    public Object buildTree(List<DeptDto> deptDtos) {
        Set<DeptDto> trees = new LinkedHashSet<>();
        Set<DeptDto> depts= new LinkedHashSet<>();
        List<String> deptNames = deptDtos.stream().map(DeptDto::getName).collect(Collectors.toList());
        boolean isChild;
        for (DeptDto deptDTO : deptDtos) {
            isChild = false;
            if (deptDTO.getPid() == null) {
                trees.add(deptDTO);
            }
            for (DeptDto it : deptDtos) {
                if (it.getPid() != null && deptDTO.getId().equals(it.getPid())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if(isChild) {
                depts.add(deptDTO);
            } else if(deptDTO.getPid() != null &&  !deptNames.contains(findById(deptDTO.getPid()).getName())) {
                depts.add(deptDTO);
            }
        }

        if (CollectionUtil.isEmpty(trees)) {
            trees = depts;
        }
        Map<String,Object> map = new HashMap<>(2);
        map.put("totalElements",deptDtos.size());
        map.put("content",CollectionUtil.isEmpty(trees)? deptDtos :trees);
        return map;
    }

    private void updateSubCnt(Long deptId){
        deptRepository= SpringUtils.getBean(DeptRepository.class);
        int count = deptRepository.countByPid(deptId);
        deptRepository.updateSubCntById(count, deptId);
    }

    @Override
    public void verification(Set<DeptDto> deptDtos) {
        UserRepository userRepository=SpringUtils.getBean(UserRepository.class);
        SysRoleRepository sysRoleRepository=SpringUtils.getBean(SysRoleRepository.class);
        Set<Long> deptIds = deptDtos.stream().map(DeptDto::getId).collect(Collectors.toSet());
        if(userRepository.countByDepts(deptIds) > 0){
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if(sysRoleRepository.countByDepts(deptIds) > 0){
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    /**
     * 清理缓存
     * @param id /
     */
    public void delCaches(Long id, Long pid){
        UserRepository userRepository=SpringUtils.getBean(UserRepository.class);
        List<User> users = userRepository.findByDeptRoleId(id);
        // 删除数据权限
        m_pRedisModule.delByKeys("data::user:",users.stream().map(User::getId).collect(Collectors.toSet()));
        m_pRedisModule.del("dept::id:" + id);
        if (pid != null) {
            m_pRedisModule.del("dept::pid:" + pid);
        }
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
