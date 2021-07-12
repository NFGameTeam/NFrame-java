package cn.yeegro.nframe.plugin.oauthcenter.logic.dao;

import cn.yeegro.nframe.plugin.oauthcenter.logic.model.SysService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author: [gitgeek]
 * @Date: [2018-08-23 12:11]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Mapper
public interface SysServiceDao {

    @Select("select p.id,p.parent_id parentId , p.name, p.path, p.sort, p.create_time createTime , p.update_time updateTime,p.is_service isService from sys_service p inner join sys_client_service rp on p.id = rp.service_id where rp.client_id = #{clientId} order by p.sort")
    List<Map> listByClientId(Long clientId);
	
    @Insert("insert into sys_service(parent_id,name,path,sort,create_time ,update_time,is_service) values (#{parentId}, #{name}, #{path},#{sort},#{createTime},#{updateTime},#{isService})")
    int save(SysService service);

    int updateByPrimaryKey(SysService service);

    @Select("select id,parent_id parentId , name, path, sort, create_time createTime , update_time updateTime,is_service isService from sys_service t where t.id = #{id}")
    SysService findById(Long id);

    @Delete("delete from sys_service where id = #{id}")
    int delete(Long id);

    @Delete("delete from sys_service where parent_id = #{id}")
    int deleteByParentId(Long id);

    @Select("select id,parent_id parentId , name, path, sort, create_time createTime , update_time updateTime,is_service isService  from sys_service t order by t.sort")
    List<SysService> findAll();

    @Select("select id,parent_id parentId , name, path, sort, create_time createTime , update_time updateTime,is_service isService  from sys_service t where t.is_service = 1 order by t.sort")
    List<SysService> findOnes();

}
