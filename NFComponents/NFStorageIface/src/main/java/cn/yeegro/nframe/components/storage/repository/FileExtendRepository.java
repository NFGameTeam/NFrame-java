package cn.yeegro.nframe.components.storage.repository;

import cn.yeegro.nframe.components.storage.model.FileExtend;
import cn.yeegro.nframe.components.storage.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FileExtendRepository extends JpaRepository<FileExtend,Long>, JpaSpecificationExecutor<FileExtend> {

    FileExtend findByMd5(String md5);

    List<FileExtend> findByGuid(String guid);



}
