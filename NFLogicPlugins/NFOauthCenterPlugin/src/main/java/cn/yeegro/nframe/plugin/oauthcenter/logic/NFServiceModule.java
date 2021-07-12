package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.common.exception.service.ServiceException;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.plugin.oauthcenter.logic.dao.SysClientServiceDao;
import cn.yeegro.nframe.plugin.oauthcenter.logic.dao.SysServiceDao;
import cn.yeegro.nframe.plugin.oauthcenter.logic.model.SysService;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIServiceModule;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Extension
public class NFServiceModule implements NFIServiceModule {


    private static NFServiceModule SingletonPtr=null;

    public static NFServiceModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFServiceModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    /**
     * 添加服务
     *
     * @param service
     */
    @Override
    public void save(SysService service) {

        SysServiceDao sysServiceDao= SpringUtils.getBean(SysServiceDao.class);

        try {
            service.setCreateTime(new Date());
            service.setUpdateTime(new Date());
            sysServiceDao.save(service);
//            log.info("添加服务：{}", service);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 更新服务
     *
     * @param service
     */
    @Override
    public void update(SysService service) {

        SysServiceDao sysServiceDao= SpringUtils.getBean(SysServiceDao.class);

        try {
            service.setUpdateTime(new Date());
            service.setUpdateTime(new Date());
            sysServiceDao.updateByPrimaryKey(service);
//            log.info("更新服务：{}", service);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 删除服务
     *
     * @param id
     */
    @Override
    public void delete(Long id) {

        SysServiceDao sysServiceDao= SpringUtils.getBean(SysServiceDao.class);

        try {
            SysService sysService = sysServiceDao.findById(id);
            sysServiceDao.deleteByParentId(sysService.getId());
            sysServiceDao.delete(id);
//            log.info("删除服务:{}",sysService);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 客户端分配服务
     *
     * @param clientId
     * @param serviceIds
     */
    @Override
    public void setMenuToClient(Long clientId, Set<Long> serviceIds) {

        SysClientServiceDao sysClientServiceDao= SpringUtils.getBean(SysClientServiceDao.class);

        try {
            sysClientServiceDao.delete(clientId,null);

            if (!CollectionUtils.isEmpty(serviceIds)){
                serviceIds.forEach(serviceId -> {
                    sysClientServiceDao.save(clientId,serviceId);
                });

            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    /**
     * 客户端服务列表
     *
     * @param clientIds
     * @return
     */
    @Override
    public List<SysService> findByClient(Set<Long> clientIds) {

        SysClientServiceDao sysClientServiceDao= SpringUtils.getBean(SysClientServiceDao.class);

        try {
            return sysClientServiceDao.findServicesBySlientIds(clientIds);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 服务列表
     *
     * @return
     */
    @Override
    public List<SysService> findAll() {
        SysServiceDao sysServiceDao= SpringUtils.getBean(SysServiceDao.class);
        try {
            return sysServiceDao.findAll();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * ID获取服务
     *
     * @param id
     * @return
     */
    @Override
    public SysService findById(Long id) {
        SysServiceDao sysServiceDao= SpringUtils.getBean(SysServiceDao.class);
        try {
            return sysServiceDao.findById(id);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 角色ID获取服务
     *
     * @param clientId
     * @return
     */
    @Override
    public Set<Long> findServiceIdsByClientId(Long clientId) {
        SysClientServiceDao sysClientServiceDao= SpringUtils.getBean(SysClientServiceDao.class);
        try {
            return sysClientServiceDao.findServiceIdsByClientId(clientId);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 一级服务
     *
     * @return
     */
    @Override
    public List<SysService> findOnes() {
        SysServiceDao sysServiceDao= SpringUtils.getBean(SysServiceDao.class);
        try {
            return sysServiceDao.findOnes();
        } catch (Exception e) {
            throw new ServiceException(e);
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
