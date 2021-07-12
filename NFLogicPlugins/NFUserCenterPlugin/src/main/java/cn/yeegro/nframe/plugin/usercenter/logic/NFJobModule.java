package cn.yeegro.nframe.plugin.usercenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.common.utils.PageUtil;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.database.exception.EntityExistException;
import cn.yeegro.nframe.components.database.util.QueryHelp;
import cn.yeegro.nframe.components.database.util.ValidationUtil;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.JobQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.JobDto;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.JobMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Job;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIJobModule;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.JobRepository;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.UserRepository;
import org.pf4j.Extension;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Extension
public class NFJobModule implements NFIJobModule {
    private NFIPluginManager pPluginManager;
    private JobRepository jobRepository;
    private JobMapper jobMapper;

    private NFIRedisModule m_pRedisModule;


    private static NFJobModule SingletonPtr=null;

    public static NFJobModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFJobModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }


    @Override
    public Map<String,Object> queryAll(JobQueryCriteria criteria, Pageable pageable) {
        jobRepository= SpringUtils.getBean(JobRepository.class);
        jobMapper= SpringUtils.getBean(JobMapper.class);
        Page<Job> page = jobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(jobMapper::toDto).getContent(),page.getTotalElements());
    }

    @Override
    public List<JobDto> queryAll(JobQueryCriteria criteria) {
        jobRepository= SpringUtils.getBean(JobRepository.class);
        jobMapper= SpringUtils.getBean(JobMapper.class);
        List<Job> list = jobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return jobMapper.toDto(list);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public JobDto findById(Long id) {
        jobRepository= SpringUtils.getBean(JobRepository.class);
        jobMapper= SpringUtils.getBean(JobMapper.class);
        Job job = jobRepository.findById(id).orElseGet(Job::new);
        ValidationUtil.isNull(job.getId(),"Job","id",id);
        return jobMapper.toDto(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Job resources) {
        jobRepository= SpringUtils.getBean(JobRepository.class);
        Job job = jobRepository.findByName(resources.getName());
        if(job != null){
            throw new EntityExistException(Job.class,"name",resources.getName());
        }
        jobRepository.save(resources);
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        jobRepository= SpringUtils.getBean(JobRepository.class);
        Job job = jobRepository.findById(resources.getId()).orElseGet(Job::new);
        Job old = jobRepository.findByName(resources.getName());
        if(old != null && !old.getId().equals(resources.getId())){
            throw new EntityExistException(Job.class,"name",resources.getName());
        }
        ValidationUtil.isNull( job.getId(),"Job","id",resources.getId());
        resources.setId(job.getId());
        jobRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        jobRepository= SpringUtils.getBean(JobRepository.class);
        jobRepository.deleteAllByIdIn(ids);
        // 删除缓存
        m_pRedisModule.delByKeys("job::id:", ids);
    }

    @Override
    public void download(List<JobDto> jobDtos, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (JobDto jobDTO : jobDtos) {
//            Map<String,Object> map = new LinkedHashMap<>();
//            map.put("岗位名称", jobDTO.getName());
//            map.put("岗位状态", jobDTO.getEnabled() ? "启用" : "停用");
//            map.put("创建日期", jobDTO.getCreateTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void verification(Set<Long> ids) {
        UserRepository userRepository=SpringUtils.getBean(UserRepository.class);
        if(userRepository.countByJobs(ids) > 0){
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
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
