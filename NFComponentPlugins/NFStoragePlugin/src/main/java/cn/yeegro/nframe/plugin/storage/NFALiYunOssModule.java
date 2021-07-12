package cn.yeegro.nframe.plugin.storage;

import cn.yeegro.nframe.components.storage.criteria.FileInfoQueryCriteria;
import cn.yeegro.nframe.components.storage.iface.NFIStorageModule;
import cn.yeegro.nframe.components.storage.model.FileInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public class NFALiYunOssModule implements NFIStorageModule {
    @Override
    public FileInfo getById(long id) {
        return null;
    }

    @Override
    public FileInfo upload(MultipartFile file) throws Exception {
        return null;
    }

    @Override
    public void delete(FileInfo fileInfo) {

    }

    @Override
    public FileInfo getByMd5(String id) {
        return null;
    }

    @Override
    public Object findList(FileInfoQueryCriteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public void unZip(String filePath, String descDir) throws RuntimeException {

    }

    @Override
    public void chunk(String guid, Integer chunk, MultipartFile file, Integer chunks, String filePath) throws Exception {

    }

    @Override
    public FileInfo merge(String guid, String fileName, String filePath) throws Exception {
        return null;
    }

    @Override
    public void uploadError(String guid, String fileName, String filePath) throws Exception {

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
