package cn.yeegro.nframe.plugin.usercenter.logic;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.exception.service.ServiceException;
import cn.yeegro.nframe.common.utils.PageUtil;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.common.utils.ValidatorUtil;
import cn.yeegro.nframe.common.web.PageResult;
import cn.yeegro.nframe.common.web.Result;
import cn.yeegro.nframe.components.database.exception.EntityExistException;
import cn.yeegro.nframe.components.database.util.QueryHelp;
import cn.yeegro.nframe.components.database.util.ValidationUtil;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import cn.yeegro.nframe.plugin.oauthcenter.logic.constant.UaaConstant;
import cn.yeegro.nframe.plugin.oauthcenter.logic.util.TokenUtil;
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.plugin.usercenter.logic.constant.UserType;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.UserQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.RoleSmallDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.UserDto;
import cn.yeegro.nframe.plugin.usercenter.logic.feign.StorageFeignClient;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.UserMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysRole;
import cn.yeegro.nframe.plugin.usercenter.logic.model.User;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIRBACModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.UserRepository;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.Extension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Extension
@Service
public class NFUserModule implements NFIUserModule {

    private NFIPluginManager pPluginManager;
    private UserRepository userRepository;
    private UserMapper userMapper;

    private NFIRedisModule m_pRedisModule;


    private static NFUserModule SingletonPtr=null;

    public static NFUserModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFUserModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public LoginAppUser getLoginAppUser() {
        // 当OAuth2AuthenticationProcessingFilter设置当前登录时，直接返回
        // 强认证时处理
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
            authentication = oAuth2Auth.getUserAuthentication();

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

                if (authenticationToken.getPrincipal() instanceof LoginAppUser) {
                    return (LoginAppUser) authenticationToken.getPrincipal();
                } else if (authenticationToken.getPrincipal() instanceof Map) {

                    LoginAppUser loginAppUser = BeanUtil.mapToBean((Map) authenticationToken.getPrincipal(), LoginAppUser.class, true);
                    Set<RoleSmallDto> roles = new HashSet<>();
                    if (CollectionUtil.isNotEmpty(loginAppUser.getSysRoles())) {
                        for(Iterator<RoleSmallDto> it = loginAppUser.getSysRoles().iterator(); it.hasNext();){
                            RoleSmallDto role =  BeanUtil.mapToBean((Map) it.next() , RoleSmallDto.class, false);
                            roles.add(role);
                        }
                    }
                    loginAppUser.setSysRoles(roles);
                    return loginAppUser;
                }
            } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                // 刷新token方式
                PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
                return (LoginAppUser) authenticationToken.getPrincipal();
            }
        }
        // 弱认证处理，当内部服务，不带token时，内部服务
        String accessToken = TokenUtil.getToken();
        if (accessToken != null) {
            RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);
            LoginAppUser loginAppUser = (LoginAppUser) redisTemplate.opsForValue().get(UaaConstant.TOKEN + ":" + accessToken);
            if (loginAppUser != null) {
                return loginAppUser;
            }
        }

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(Set<RoleSmallDto> sysRoles,Set<String> permissions) {
        Collection<GrantedAuthority> collection = new HashSet<>();
        Collection<GrantedAuthority> synchronizedCollection = Collections.synchronizedCollection(collection);
        if (!CollectionUtils.isEmpty(sysRoles)) {
            sysRoles.parallelStream().forEach(role -> {
                if (role.getCode().startsWith("ROLE_")) {
                    synchronizedCollection.add(new SimpleGrantedAuthority(role.getCode()));
                } else {
                    synchronizedCollection.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
                }
            });
        }

        if (!CollectionUtils.isEmpty(permissions)) {
            permissions.parallelStream().forEach(per -> {
                synchronizedCollection.add(new SimpleGrantedAuthority(per));
            });
        }
        return collection;
    }

    @Override
    public Collection<? extends GrantedAuthority> putAll(Collection<GrantedAuthority> collections) {
        Collection<GrantedAuthority> collection = new HashSet<>();
        collection.addAll(collections);
        return collection;
    }

    @Override
    @Transactional
    @Cacheable(key = "'id:' + #p0")
    public UserDto findById(long id) {
        User user = userRepository.findById(id).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(),"User","id",id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(User resources) {
        if(userRepository.findByUsername(resources.getUsername())!=null){
            throw new EntityExistException(User.class,"username",resources.getUsername());
        }
        if(userRepository.findByEmail(resources.getEmail())!=null){
            throw new EntityExistException(User.class,"email",resources.getEmail());
        }
        userRepository.save(resources);
    }

    @Override
    public void update(User resources) {
        userRepository=SpringUtils.getBean(UserRepository.class);
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(),"User","id",resources.getId());
        User user1 = userRepository.findByUsername(resources.getUsername());
        User user2 = userRepository.findByEmail(resources.getEmail());

        if(user1 !=null&&!user.getId().equals(user1.getId())){
            throw new EntityExistException(User.class,"username",resources.getUsername());
        }

        if(user2!=null&&!user.getId().equals(user2.getId())){
            throw new EntityExistException(User.class,"email",resources.getEmail());
        }
        // 如果用户的角色改变
        if (!resources.getRoles().equals(user.getRoles())) {
            m_pRedisModule.del("data::user:" + resources.getId());
            m_pRedisModule.del("menu::user:" + resources.getId());
            m_pRedisModule.del("role::auth:" + resources.getId());
        }
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());
        user.setDept(resources.getDept());
        user.setJobs(resources.getJobs());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setGender(resources.getGender());
        userRepository.save(user);
        // 清除缓存
        delCaches(user.getId(), user.getUsername());
    }

    @Override
    public void save(UserDto user) {
        userRepository=SpringUtils.getBean(UserRepository.class);
        userMapper=SpringUtils.getBean(UserMapper.class);
        User _user =userMapper.toEntity(user);
        userRepository.save(_user);
    }

    @Override
    public void updateCenter(User resources) {
        userRepository=SpringUtils.getBean(UserRepository.class);
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        userRepository.save(user);
        // 清理缓存
        delCaches(user.getId(), user.getUsername());
    }

    @Override
    public void delete(Set<Long> ids) {
        userRepository=SpringUtils.getBean(UserRepository.class);
        for (Long id : ids) {
            // 清理缓存
            UserDto user = findById(id);
            delCaches(user.getId(), user.getUsername());
        }
        userRepository.deleteAllByIdIn(ids);
    }

    @Override
    public UserDto findByName(String userName) {
        userRepository=SpringUtils.getBean(UserRepository.class);
        userMapper=SpringUtils.getBean(UserMapper.class);
        User user = userRepository.findByUsername(userName);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto findByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            return null;
        } else {
            return userMapper.toDto(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userRepository.updatePass(username,pass,new Date());
        m_pRedisModule.del("user::username:" + username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {

        StorageFeignClient storageFeignClient=SpringUtils.getBean(StorageFeignClient.class);
        Object aaa= storageFeignClient.upload(multipartFile);
        User user = userRepository.findByUsername(getLoginAppUser().getUsername());
//        String oldPath = user.getAvatarPath();
//        File file = FileUtil.upload(multipartFile, properties.getPath().getAvatar());
//        user.setAvatarPath(Objects.requireNonNull(file).getPath());
//        user.setAvatarName(file.getName());
//        userRepository.save(user);
//        if(StringUtils.isNotBlank(oldPath)){
//            FileUtil.del(oldPath);
//        }
//        m_pRedisModule.del("user::username:" + user.getUsername());
//        return new HashMap<String,String>(){{put("avatar",file.getName());}};
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userRepository.updateEmail(username,email);
        m_pRedisModule.del("user::username:" + username);
    }

    @Override
    public Object queryAll(UserQueryCriteria criteria, Pageable pageable) {
        userRepository=SpringUtils.getBean(UserRepository.class);
        userMapper=SpringUtils.getBean(UserMapper.class);
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(userMapper::toDto));
    }


    @Override
    public List<UserDto> queryAll(UserQueryCriteria criteria) {
        List<User> users = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return userMapper.toDto(users);
    }

    @Override
    public void download(List<UserDto> queryAll, HttpServletResponse response) throws IOException {

    }

    /**
     * 清理缓存
     * @param id /
     */
    public void delCaches(Long id, String username){
        m_pRedisModule.del("user::id:" + id);
        m_pRedisModule.del("user::username:" + username);
    }
    
    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRedisModule= pPluginManager.FindModule(NFIRedisModule.class);
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
