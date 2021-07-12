package cn.yeegro.nframe.user.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.annotation.AnonymousAccess;
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.common.utils.PageUtil;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.util.TokenUtil;
import cn.yeegro.nframe.plugin.usercenter.logic.config.RsaProperties;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.UserQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.RoleSmallDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.UserDto;
import cn.yeegro.nframe.plugin.usercenter.logic.feign.StorageFeignClient;
import cn.yeegro.nframe.plugin.usercenter.logic.model.SysPermission;
import cn.yeegro.nframe.plugin.usercenter.logic.model.User;
import cn.yeegro.nframe.plugin.usercenter.logic.model.vo.UserPassVo;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIRBACModule;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIUserModule;
import cn.yeegro.nframe.plugin.usercenter.logic.util.RsaUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import cn.yeegro.nframe.plugin.usercenter.logic.auth.details.LoginAppUser;
import cn.yeegro.nframe.common.exception.controller.ControllerException;
import cn.yeegro.nframe.common.exception.service.ServiceException;
import cn.yeegro.nframe.log.annotation.LogAnnotation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 用户
 */
@Slf4j
@RestController
@Api(tags = "USER API")
@RequiredArgsConstructor
public class SysUserController {


    private NFIPluginManager pPluginManager;
    private NFIUserModule m_pSysUserModule;
    private NFIRBACModule m_pRBACModule;

    @Autowired
    private final StorageFeignClient storageFeignClient;

    /**
     * 当前登录用户 LoginAppUser
     *
     * @return
     * @throws ControllerException
     * @throws JsonProcessingException
     */
    @ApiOperation(value = "根据access_token当前登录用户")
    @GetMapping("/users/current")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public LoginAppUser getLoginAppUser() throws ControllerException {
        try {
            pPluginManager = NFPluginManager.GetSingletonPtr();
            m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
            LoginAppUser loginUser = m_pSysUserModule.getLoginAppUser();
            return loginUser;
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping(value = "/users-anon/login", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public LoginAppUser findByUsername(String username) throws ControllerException {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        try {
            UserDto sysUser = m_pSysUserModule.findByName(username);
            if (sysUser != null) {
                LoginAppUser loginAppUser = new LoginAppUser();
                BeanUtils.copyProperties(sysUser, loginAppUser);

                Set<RoleSmallDto> sysRoles =sysUser.getRoles();
                loginAppUser.setSysRoles(sysRoles);// 设置角色

                if (!CollectionUtils.isEmpty(sysRoles)) {

                    for (RoleSmallDto role:sysRoles) {
                        Set<SysPermission> sysPermissions=role.getPermissions();
                        if (!CollectionUtils.isEmpty(sysPermissions)) {
                            Set<String> permissions = sysPermissions.parallelStream().map(p -> p.getCode())
                                    .collect(Collectors.toSet());
                            loginAppUser.setPermissions(permissions);// 设置权限集合
                        }
                    }

                }

                return loginAppUser;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        return null;

    }

    @ApiOperation("退出登录")
    @AnonymousAccess
    @DeleteMapping(value = "/out")
    public ResponseEntity<Object> logout(HttpServletRequest request){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);

        redisTemplate.delete(TokenUtil.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation("查询用户")
    @GetMapping("/users")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<Object> query(UserQueryCriteria criteria, Pageable pageable){

        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        LoginAppUser loginUser = m_pSysUserModule.getLoginAppUser();

        // 数据权限
        List<Long> dataScopes = m_pRBACModule.getDeptIds(m_pSysUserModule.findByName(loginUser.getUsername()));
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(criteria.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)){
            // 取交集
            criteria.getDeptIds().retainAll(dataScopes);
            if(!CollectionUtil.isEmpty(criteria.getDeptIds())){
                return new ResponseEntity<>(m_pSysUserModule.queryAll(criteria,pageable), HttpStatus.OK);
            }
        } else {
            // 否则取并集
            criteria.getDeptIds().addAll(dataScopes);
            return new ResponseEntity<>(m_pSysUserModule.queryAll(criteria,pageable),HttpStatus.OK);
        }
        return new ResponseEntity<>(PageUtil.toPage(null,0),HttpStatus.OK);
    }

    @ApiOperation("新增用户")
    @PostMapping("/users")
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody User resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        PasswordEncoder passwordEncoder= SpringUtils.getBean(PasswordEncoder.class);
        checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        m_pSysUserModule.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改用户")
    @PutMapping("/users")
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<Object> update(@Validated(User.Update.class) @RequestBody User resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        checkLevel(resources);
        m_pSysUserModule.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> center(@Validated(User.Update.class) @RequestBody User resources){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        if(!resources.getId().equals(m_pSysUserModule.getLoginAppUser().getId())){
            throw new BadRequestException("不能修改他人资料");
        }
        m_pSysUserModule.updateCenter(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/users")
    @PreAuthorize("@el.check('user:del')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        for (Long id : ids) {
            Integer currentLevel =  Collections.min(m_pRBACModule.findByUsersId(m_pSysUserModule.getLoginAppUser().getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel =  Collections.min(m_pRBACModule.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + m_pSysUserModule.findById(id).getUsername());
            }
        }
        m_pSysUserModule.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/users/updatePass")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> updatePass(@RequestBody UserPassVo passVo) throws Exception {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        PasswordEncoder passwordEncoder= SpringUtils.getBean(PasswordEncoder.class);
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,passVo.getNewPass());
        UserDto user = m_pSysUserModule.findByName(m_pSysUserModule.getLoginAppUser().getUsername());
        if(!passwordEncoder.matches(oldPass, user.getPassword())){
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if(passwordEncoder.matches(newPass, user.getPassword())){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        m_pSysUserModule.updatePass(user.getUsername(),passwordEncoder.encode(newPass));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/users/updateAvatar")
    public ResponseEntity<Object> updateAvatar(@RequestParam MultipartFile avatar){
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        ResponseEntity<Object> m_Response=storageFeignClient.upload(avatar);

        //{"id":17,"md5":"9d74fb100a6f1a9c5d7ce942baee595b","name":"avatar.png","isImg":true,"contentType":"image/png","size":84723,"source":"FASTDFS","createTime":"2021-05-12T00:36:38.347+0000"}
        String fileInfo=JSON.toJSONString(m_Response.getBody());
        JSONObject o_fileInfo= JSONObject.parseObject(fileInfo);
        UserDto user = m_pSysUserModule.findByName(m_pSysUserModule.getLoginAppUser().getUsername());
        user.setAvatarPath(o_fileInfo.getString("url"));
        m_pSysUserModule.save(user);

        return new ResponseEntity<>(m_Response,HttpStatus.OK);
    }

    @ApiOperation("修改邮箱")
    @PostMapping(value = "/users/updateEmail/{code}")
    public ResponseEntity<Object> updateEmail(@PathVariable String code,@RequestBody User user) throws Exception {
        pPluginManager = NFPluginManager.GetSingletonPtr();
//        m_pVerifyModule= pPluginManager.FindModule(NFIValidateCodeService.class);
        PasswordEncoder passwordEncoder= SpringUtils.getBean(PasswordEncoder.class);
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,user.getPassword());
        UserDto userDto = m_pSysUserModule.findByName(m_pSysUserModule.getLoginAppUser().getUsername());
        if(!passwordEncoder.matches(password, userDto.getPassword())){
            throw new BadRequestException("密码错误");
        }
//        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        m_pSysUserModule.updateEmail(userDto.getUsername(),user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     * @param resources /
     */
    private void checkLevel(User resources) {
        pPluginManager = NFPluginManager.GetSingletonPtr();
        m_pRBACModule= pPluginManager.FindModule(NFIRBACModule.class);
        m_pSysUserModule = pPluginManager.FindModule(NFIUserModule.class);
        Integer currentLevel =  Collections.min(m_pRBACModule.findByUsersId(m_pSysUserModule.getLoginAppUser().getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        Integer optLevel = m_pRBACModule.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

}
