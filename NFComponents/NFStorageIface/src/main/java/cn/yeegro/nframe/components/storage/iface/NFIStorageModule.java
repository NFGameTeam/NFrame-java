package cn.yeegro.nframe.components.storage.iface;

import cn.yeegro.nframe.comm.code.module.NFIModule;
import cn.yeegro.nframe.components.storage.criteria.FileInfoQueryCriteria;
import cn.yeegro.nframe.components.storage.model.FileInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface NFIStorageModule extends NFIModule {

    FileInfo getById(long id);

    FileInfo upload(MultipartFile file) throws Exception;

    void delete(FileInfo fileInfo);

    FileInfo getByMd5(String id);

    Object findList(FileInfoQueryCriteria criteria, Pageable pageable);

    void unZip(String filePath, String descDir) throws RuntimeException ;

    void chunk(String guid, Integer chunk, MultipartFile file, Integer chunks, String filePath) throws Exception;

    FileInfo merge(String guid, String fileName, String filePath) throws Exception;

    void uploadError(String guid, String fileName, String filePath) throws Exception;

}
