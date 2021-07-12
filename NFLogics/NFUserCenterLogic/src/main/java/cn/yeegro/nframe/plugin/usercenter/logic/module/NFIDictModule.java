package cn.yeegro.nframe.plugin.usercenter.logic.module;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.DictDetailQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.DictQueryCriteria;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.DictDetailDto;
import cn.yeegro.nframe.plugin.usercenter.logic.dto.DictDto;
import cn.yeegro.nframe.plugin.usercenter.logic.model.Dict;
import cn.yeegro.nframe.plugin.usercenter.logic.model.DictDetail;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NFIDictModule extends NFIModule {

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Map<String,Object> queryAll(DictQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部数据
     * @param dict /
     * @return /
     */
    List<DictDto> queryAll(DictQueryCriteria dict);

    /**
     * 创建
     * @param resources /
     * @return /
     */
    void create(Dict resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Dict resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DictDto> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 创建
     * @param resources /
     */
    void create(DictDetail resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(DictDetail resources);

    /**
     * 删除
     * @param id /
     */
    void delete(Long id);

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Map<String,Object> queryAll(DictDetailQueryCriteria criteria, Pageable pageable);

    /**
     * 根据字典名称获取字典详情
     * @param name 字典名称
     * @return /
     */
    List<DictDetailDto> getDictByName(String name);

}
