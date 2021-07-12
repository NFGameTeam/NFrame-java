package cn.yeegro.nframe.plugin.usercenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.UserQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.RoleSmallDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.UserDto;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysRole;
import cn.yeegro.nframe.plugin.usercenter.logic.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NFIUserModule extends NFIModule {

    /**
     * 获取登陆的 LoginAppUser
     *
     * @return
     */
    LoginAppUser getLoginAppUser();

    /***
     * 权限重写
     */
    Collection<? extends GrantedAuthority> getAuthorities(Set<RoleSmallDto> sysRoles, Set<String> permissions);

    /***
     * 权限重写
     */
    Collection<? extends GrantedAuthority> putAll(Collection<GrantedAuthority> collections);




    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    UserDto findById(long id);

    /**
     * 新增用户
     * @param resources /
     */
    void create(User resources);

    /**
     * 编辑用户
     * @param resources /
     */
    void update(User resources);


    void save(UserDto user);

    /**
     * 删除用户
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 根据用户名查询
     * @param userName /
     * @return /
     */
    UserDto findByName(String userName);

    /**
     * 根据用户名查询
     * @param phone /
     * @return /
     */
    UserDto findByPhone(String phone);

    /**
     * 修改密码
     * @param username 用户名
     * @param encryptPassword 密码
     */
    void updatePass(String username, String encryptPassword);

    /**
     * 修改头像
     * @param file 文件
     * @return /
     */
    Map<String, String> updateAvatar(MultipartFile file);

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    void updateEmail(String username, String email);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(UserQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<UserDto> queryAll(UserQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<UserDto> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 用户自助修改资料
     * @param resources /
     */
    void updateCenter(User resources);




}
