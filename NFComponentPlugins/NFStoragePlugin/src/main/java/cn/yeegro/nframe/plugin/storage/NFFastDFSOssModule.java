package cn.yeegro.nframe.plugin.storage;

import cn.hutool.core.util.StrUtil;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.storage.iface.NFIFastDFSOssModule;
import cn.yeegro.nframe.components.storage.model.FileExtend;
import cn.yeegro.nframe.components.storage.model.FileInfo;
import cn.yeegro.nframe.components.storage.model.FileType;
import cn.yeegro.nframe.components.storage.repository.FileExtendRepository;
import cn.yeegro.nframe.components.storage.repository.FileInfoRepository;
import cn.yeegro.nframe.plugin.storage.invoke.FastdfsClientUtil;
import cn.yeegro.nframe.components.storage.utils.FileUtil;

import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.pf4j.Extension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Extension
public class NFFastDFSOssModule extends NFIFastDFSOssModule {


    private static NFFastDFSOssModule SingletonPtr=null;

    public static NFFastDFSOssModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFFastDFSOssModule();
            return SingletonPtr;
        }
        return SingletonPtr;
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

    @Override
    protected FileType fileType() {
        return FileType.FASTDFS;
    }

    @Override
    protected FileInfo uploadFile(MultipartFile multipartFile, FileInfo fileInfo) throws Exception {

        FileInfoRepository fileInfoRepository= SpringUtils.getBean(FileInfoRepository.class);

        File file = FileUtil.toFile(multipartFile);
        // 验证是否重复上传
        FileInfo picture = fileInfoRepository.findByMd5(FileUtil.getMd5(file));
        if(picture != null){
            return picture;
        }
        else {
            picture=new FileInfo();
            picture.setSize(multipartFile.getSize());
        }
        HashMap<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("smfile", file);

        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String url = FastdfsClientUtil.upload(multipartFile.getOriginalFilename(),input).trim();

        if (url!=null) {
            picture.setUrl(url);
            picture.setSize(picture.getSize());
            picture.setMd5(FileUtil.getMd5(file));
            picture.setName(FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) + "." + FileUtil.getExtensionName(multipartFile.getOriginalFilename()));
            picture= fileInfoRepository.save(picture);
        }
        //删除临时文件
        FileUtil.deleteFile(file.getName());
        return picture;

    }

    @Override
    protected boolean deleteFile(FileInfo fileInfo) {
        if (fileInfo != null && StrUtil.isNotEmpty(fileInfo.getPath())) {
            FastdfsClientUtil.delete(fileInfo.getSource());
        }
        return true;
    }

    /**
     * 上传大文件
     * 分片上传 每片一个临时文件
     *
     * @param guid
     * @param chunk
     * @param file
     * @param chunks
     * @return
     */
    @Override
    protected void chunkFile(String guid, Integer chunk, MultipartFile file, Integer chunks,String filePath)throws Exception {
//        log.info("guid:{},chunkNumber:{}",guid,chunk);

        FileExtendRepository fileExtendRepository= SpringUtils.getBean(FileExtendRepository.class);

        if(Objects.isNull(chunk)){
            chunk = 0;
        }

        // TODO: 2020/6/16 从RequestContextHolder上下文中获取 request对象
        boolean isMultipart = ServletFileUpload.isMultipartContent(((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest());
        if (isMultipart) {
            StringBuffer tempFilePath = new StringBuffer();
            tempFilePath.append(guid).append("_").append(chunk).append(".part");
            FileExtend fileExtend = new FileExtend();
            String md5 = FileUtil.fileMd5(file.getInputStream());
            fileExtend.setMd5(md5);
            fileExtend.setGuid(guid);
            fileExtend.setSize(file.getSize());
            fileExtend.setName(tempFilePath.toString());
            fileExtend.setSource(fileType().name());

            FileExtend oldFileExtend = fileExtendRepository.findByMd5(fileExtend.getMd5());
            if (oldFileExtend != null) {
                return;
            }

            // TODO: 2020/6/29 fastdfs上传
          //  StorePath storePath = FastdfsClientUtil.upload(file.getInputStream(), file.getSize(),  FilenameUtils.getExtension(tempFilePath.toString()), null);
           // fileExtend.setUrl(storePath.getFullPath());
           // fileExtend.setPath(storePath.getFullPath());

            fileExtendRepository.save(fileExtend);
        }
    }


    /**
     * 合并分片文件
     * 每一个小片合并一个完整文件
     *
     * @param guid
     * @param fileName
     * @param filePath
     * @return
     */
    @Override
    protected FileInfo mergeFile(String guid, String fileName, String filePath) throws Exception {
        // 得到 destTempFile 就是最终的文件
//        log.info("guid:{},fileName:{}",guid,fileName);
        FileInfoRepository fileInfoRepository= SpringUtils.getBean(FileInfoRepository.class);
        FileExtendRepository fileExtendRepository= SpringUtils.getBean(FileExtendRepository.class);

        //根据guid 获取 全部临时分片数据
        List<FileExtend> fileExtends = fileExtendRepository.findByGuid(guid);
//        log.info("fileExtends -> size ：{}",fileExtends.size());

        File parentFileDir = new File(filePath + File.separator + guid);
        File destTempFile = new File(filePath , fileName);
        try {
            if (CollectionUtils.isEmpty(fileExtends)){
                return null;
            }

            // TODO: 2020/6/29 下载到本地进行操作
            for (FileExtend extend:  fileExtends) {
                DownloadByteArray callback = new DownloadByteArray();
             //   byte[] buf = FastdfsClientUtil.downloadFile("group1", extend.getPath().substring(extend.getPath().lastIndexOf("group1/")+7),callback);
             //   FileUtil.byte2File(buf,filePath + File.separator + guid,extend.getName());
            }

            FileUtil.saveBigFile(guid, parentFileDir, destTempFile);

            // TODO: 2020/6/17 保存到数据库中 FASTDFS
            FileInputStream fileInputStream = new FileInputStream(destTempFile);
            MultipartFile multipartFile = new MockMultipartFile(destTempFile.getName(), destTempFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

            FileInfo fileInfo = FileUtil.getFileInfo(multipartFile);
            fileInfo.setName(fileName);
            FileInfo oldFileInfo = fileInfoRepository.findByMd5(fileInfo.getMd5());

            if (oldFileInfo != null) {
                return oldFileInfo;
            }

          //  StorePath storePath = FastdfsClientUtil.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), FilenameUtils.getExtension(multipartFile.getOriginalFilename()), null);
         //   fileInfo.setUrl(domain+ storePath.getFullPath());
           // fileInfo.setPath(storePath.getFullPath());

            fileInfo.setSource(fileType().name());// 设置文件来源
            fileInfoRepository.save(fileInfo);// 将文件信息保存到数据库

            // TODO: 2020/6/29 更新分片文件的FileId
            fileExtends.stream().forEach(vo->vo.setId(fileInfo.getId()));
            fileExtendRepository.saveAll(fileExtends);
            return  fileInfo;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            // 删除临时目录中的分片文件
            try {
                destTempFile.delete();
                FileUtils.deleteDirectory(parentFileDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
