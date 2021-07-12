package cn.yeegro.nframe.plugin.usercenter.logic;

import cn.hutool.core.collection.CollectionUtil;
import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.PageUtil;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.database.util.QueryHelp;
import cn.yeegro.nframe.components.database.util.ValidationUtil;
import cn.yeegro.nframe.components.nosql.redis.NFIRedisModule;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.DictDetailQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.DictQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.DictDetailDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.DictDto;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.DictDetailMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.mapstruct.DictMapper;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Dict;
import cn.yeegro.nframe.plugin.usercenter.logic.model.DictDetail;
import cn.yeegro.nframe.plugin.usercenter.logic.module.NFIDictModule;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.DictDetailRepository;
import cn.yeegro.nframe.plugin.usercenter.logic.repository.DictRepository;
import org.pf4j.Extension;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Extension
public class NFDictModule implements NFIDictModule {

    private NFIPluginManager pPluginManager;
    private DictRepository dictRepository;
    private DictMapper dictMapper;

    private DictDetailRepository dictDetailRepository;
    private DictDetailMapper dictDetailMapper;
    private NFIRedisModule m_pRedisModule;


    private static NFDictModule SingletonPtr=null;

    public static NFDictModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFDictModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public Map<String, Object> queryAll(DictQueryCriteria dict, Pageable pageable){
        dictRepository= SpringUtils.getBean(DictRepository.class);
        dictMapper= SpringUtils.getBean(DictMapper.class);
        Page<Dict> page = dictRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, dict, cb), pageable);
        return PageUtil.toPage(page.map(dictMapper::toDto));
    }

    @Override
    public List<DictDto> queryAll(DictQueryCriteria dict) {
        dictRepository= SpringUtils.getBean(DictRepository.class);
        dictMapper= SpringUtils.getBean(DictMapper.class);
        List<Dict> list = dictRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, dict, cb));
        return dictMapper.toDto(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Dict resources) {
        dictRepository= SpringUtils.getBean(DictRepository.class);
        dictRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dict resources) {
        dictRepository= SpringUtils.getBean(DictRepository.class);
        // 清理缓存
        delCaches(resources);
        Dict dict = dictRepository.findById(resources.getId()).orElseGet(Dict::new);
        ValidationUtil.isNull( dict.getId(),"Dict","id",resources.getId());
        resources.setId(dict.getId());
        dictRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        dictRepository= SpringUtils.getBean(DictRepository.class);
        // 清理缓存
        List<Dict> dicts = dictRepository.findByIdIn(ids);
        for (Dict dict : dicts) {
            delCaches(dict);
        }
        dictRepository.deleteByIdIn(ids);
    }

    @Override
    public void download(List<DictDto> dictDtos, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (DictDto dictDTO : dictDtos) {
//            if(CollectionUtil.isNotEmpty(dictDTO.getDictDetails())){
//                for (DictDetailDto dictDetail : dictDTO.getDictDetails()) {
//                    Map<String,Object> map = new LinkedHashMap<>();
//                    map.put("字典名称", dictDTO.getName());
//                    map.put("字典描述", dictDTO.getDescription());
//                    map.put("字典标签", dictDetail.getLabel());
//                    map.put("字典值", dictDetail.getValue());
//                    map.put("创建日期", dictDetail.getCreateTime());
//                    list.add(map);
//                }
//            } else {
//                Map<String,Object> map = new LinkedHashMap<>();
//                map.put("字典名称", dictDTO.getName());
//                map.put("字典描述", dictDTO.getDescription());
//                map.put("字典标签", null);
//                map.put("字典值", null);
//                map.put("创建日期", dictDTO.getCreateTime());
//                list.add(map);
//            }
//        }
//        FileUtil.downloadExcel(list, response);
    }

    public void delCaches(Dict dict){
        m_pRedisModule.del("dept::name:" + dict.getName());
    }

    @Override
    public Map<String,Object> queryAll(DictDetailQueryCriteria criteria, Pageable pageable) {

        dictDetailRepository= SpringUtils.getBean(DictDetailRepository.class);
        dictDetailMapper= SpringUtils.getBean(DictDetailMapper.class);

        Page<DictDetail> page = dictDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dictDetailMapper::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DictDetail resources) {
        dictDetailRepository= SpringUtils.getBean(DictDetailRepository.class);
        dictDetailRepository.save(resources);
        // 清理缓存
        delCaches(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetail resources) {
        dictDetailRepository= SpringUtils.getBean(DictDetailRepository.class);
        DictDetail dictDetail = dictDetailRepository.findById(resources.getId()).orElseGet(DictDetail::new);
        ValidationUtil.isNull( dictDetail.getId(),"DictDetail","id",resources.getId());
        resources.setId(dictDetail.getId());
        dictDetailRepository.save(resources);
        // 清理缓存
        delCaches(resources);
    }

    @Override
    @Cacheable(key = "'name:' + #p0")
    public List<DictDetailDto> getDictByName(String name) {
        dictDetailRepository= SpringUtils.getBean(DictDetailRepository.class);
        dictDetailMapper= SpringUtils.getBean(DictDetailMapper.class);
        return dictDetailMapper.toDto(dictDetailRepository.findByDictName(name));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dictDetailRepository= SpringUtils.getBean(DictDetailRepository.class);
        DictDetail dictDetail = dictDetailRepository.findById(id).orElseGet(DictDetail::new);
        // 清理缓存
        delCaches(dictDetail);
        dictDetailRepository.deleteById(id);
    }

    public void delCaches(DictDetail dictDetail){
        dictRepository= SpringUtils.getBean(DictRepository.class);
        Dict dict = dictRepository.findById(dictDetail.getDict().getId()).orElseGet(Dict::new);
        m_pRedisModule.del("dept::name:" + dict.getName());
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
