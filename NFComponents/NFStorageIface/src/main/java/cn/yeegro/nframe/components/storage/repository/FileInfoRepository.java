package cn.yeegro.nframe.components.storage.repository;

import cn.yeegro.nframe.components.storage.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FileInfoRepository extends JpaRepository<FileInfo,Long>, JpaSpecificationExecutor<FileInfo> {

    FileInfo findByMd5(String md5);

}
