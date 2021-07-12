package cn.yeegro.nframe.components.storage.iface;

import cn.yeegro.nframe.common.utils.PageUtil;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.database.util.QueryHelp;
import cn.yeegro.nframe.components.storage.criteria.FileInfoQueryCriteria;
import cn.yeegro.nframe.components.storage.iface.NFIStorageModule;
import cn.yeegro.nframe.components.storage.mapstruct.FileInfoMapper;
import cn.yeegro.nframe.components.storage.model.FileInfo;
import cn.yeegro.nframe.components.storage.model.FileType;
import cn.yeegro.nframe.components.storage.repository.FileInfoRepository;
import cn.yeegro.nframe.components.storage.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class NFStorageModule implements NFIStorageModule {

    private FileInfoRepository fileInfoRepository;

    private FileInfoMapper fileInfoMapper;


    @Override
    public FileInfo getById(long id) {
        FileInfo fileInfo=fileInfoRepository.findById(id).orElseGet(FileInfo::new);;
        return fileInfo;
    }
    /**
     * 文件来源
     *
     * @return
     */
    protected abstract FileType fileType();

    /**
     * 上传文件
     *
     * @param file
     * @param fileInfo
     */
    protected abstract FileInfo uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception;
    /**
     * 删除文件资源
     *
     * @param fileInfo
     * @return
     */
    protected abstract boolean deleteFile(FileInfo fileInfo);

    /**
     * 上传大文件
     *		分片上传 每片一个临时文件
     * @param file
     * @return
     */
    protected abstract void chunkFile( String guid, Integer chunk, MultipartFile file, Integer chunks,String filePath) throws Exception;

    /**
     * 合并分片文件
     *		每一个小片合并一个完整文件
     * @param fileName
     * @return
     */
    protected abstract FileInfo mergeFile( String guid,String fileName,String filePath ) throws Exception;

    /**
     * 失败回调
     * @param guid
     * @param fileName
     * @param filePath
     * @throws Exception
     */
//	protected abstract void uploadError( String guid,String fileName,String filePath ) throws Exception;

    protected static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public FileInfo upload(MultipartFile file) throws Exception {

        fileInfoRepository= SpringUtils.getBean(FileInfoRepository.class);

        FileInfo fileInfo = FileUtil.getFileInfo(file);
        FileInfo oldFileInfo = fileInfoRepository.findByMd5(fileInfo.getMd5());
        if (oldFileInfo != null) {
            return oldFileInfo;
        }

        if (!fileInfo.getName().contains(".")) {
            throw new IllegalArgumentException("缺少后缀名");
        }

        fileInfo=uploadFile(file, fileInfo);

        fileInfo.setSource(fileType().name());// 设置文件来源
        fileInfo=fileInfoRepository.save(fileInfo);// 将文件信息保存到数据库
//		// 本地保存文件
//		FileUtil.saveFile(file,fileInfo.getPath());
//        log.info("上传文件：{}", fileInfo);

        return fileInfo;
    }


    @Override
    public void delete(FileInfo fileInfo) {
        fileInfoRepository= SpringUtils.getBean(FileInfoRepository.class);
        deleteFile(fileInfo);
        fileInfoRepository.delete(fileInfo);
//        log.info("删除文件：{}", fileInfo);
    }

    @Override
    public FileInfo getByMd5(String md5){
        fileInfoRepository= SpringUtils.getBean(FileInfoRepository.class);
        return fileInfoRepository.findByMd5(md5);
    }

    @Override
    public Object findList(FileInfoQueryCriteria criteria, Pageable pageable){
        fileInfoRepository= SpringUtils.getBean(FileInfoRepository.class);
        fileInfoMapper= SpringUtils.getBean(FileInfoMapper.class);
        Page<FileInfo> page = fileInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(fileInfoMapper::toDto));

    }

    @Override
    public void unZip(String filePath, String descDir) throws RuntimeException {

    }


    @Override
    public void chunk(String guid, Integer chunk, MultipartFile file, Integer chunks,String filePath) throws Exception {
        // TODO: 2020/6/16  分片提交
        chunkFile(guid,chunk,file,chunks,filePath);
    }

    @Override
    public FileInfo merge(String guid, String fileName, String filePath) throws Exception {
        return mergeFile(guid,fileName,filePath);
    }

    @Override
    public void uploadError(String guid, String fileName, String filePath) throws Exception {
        File parentFileDir = new File(filePath + File.separator + guid);
        try {
        }finally {
            // 删除临时目录中的分片文件
            try {
                FileUtils.deleteDirectory(parentFileDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
