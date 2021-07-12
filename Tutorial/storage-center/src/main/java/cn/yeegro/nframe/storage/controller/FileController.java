package cn.yeegro.nframe.storage.controller;

import cn.yeegro.nframe.common.web.PageResult;
import cn.yeegro.nframe.common.web.Result;
import cn.yeegro.nframe.components.storage.criteria.FileInfoQueryCriteria;
import cn.yeegro.nframe.components.storage.iface.NFIStorageModule;
import cn.yeegro.nframe.components.storage.model.FileInfo;
import cn.yeegro.nframe.components.storage.model.FileType;
import cn.yeegro.nframe.components.storage.model.MergeFileDTO;
import cn.yeegro.nframe.log.annotation.LogAnnotation;
import cn.yeegro.nframe.plugin.usercenter.logic.criteria.UserQueryCriteria;
import cn.yeegro.nframe.storage.config.OssServiceFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
*  文件上传 同步oss db双写 目前仅实现了阿里云,七牛云
*  参考src/main/view/upload.html
*/
@RestController
@Api(tags = "FILE API")
@Slf4j
public class FileController {

	@Autowired
	private OssServiceFactory fileServiceFactory;


	/**
	 * 文件上传
	 * 根据fileType选择上传方式
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/files-anon")
	@LogAnnotation(module = "file-center", recordRequestParam = false)
	public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) throws Exception {
		
		String fileType = FileType.FASTDFS.toString();
		NFIStorageModule m_pStorageModule = fileServiceFactory.getFileModule(fileType);

		FileInfo fileInfo=m_pStorageModule.upload(file);

		return new ResponseEntity<Object>(fileInfo,HttpStatus.OK);
	}

	/**
	 * layui富文本文件自定义上传
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/files/layui")
	@LogAnnotation(module = "file-center", recordRequestParam = false)
	public Map<String, Object> uploadLayui(@RequestParam("file") MultipartFile file )
			throws Exception {
		
		FileInfo fileInfo = (FileInfo)upload(file).getBody();

		Map<String, Object> map = new HashMap<>();
		map.put("code", 0);
		Map<String, Object> data = new HashMap<>();
		data.put("src", fileInfo.getUrl());
		map.put("data", data);

		return map;
	}

	/**
	 * 文件删除
	 * @param id
	 */
	@DeleteMapping("/files/{id}")
	@PreAuthorize("hasAuthority('file:del')") 
	@LogAnnotation(module = "file-center", recordRequestParam = false)
	public Result delete(@PathVariable long id) {

//		try{
//			FileInfo fileInfo = fileServiceFactory.getFileService(FileType.FASTDFS.toString()).getById(id);
//			if (fileInfo != null) {
//				FileService fileService = fileServiceFactory.getFileService(fileInfo.getSource());
//				fileService.delete(fileInfo);
//			}
//			return Result.succeed("操作成功");
//		}catch (Exception ex){
//			return Result.failed("操作失败");
//		}
		return null;
	}
 
	/**
	 * 文件查询
	 * @param criteria
	 * @return
	 * @throws JsonProcessingException 
	 */
	@GetMapping("/files")
	@PreAuthorize("hasAuthority('file:query')")
	public ResponseEntity<?> findFiles(FileInfoQueryCriteria criteria, Pageable pageable){

		return null;
		//return new ResponseEntity<>(fileServiceFactory.getFileService(FileType.FASTDFS.toString()).findList(criteria,pageable), HttpStatus.OK);
	}


	/**
	 * 	注意： 上传大文件为2个方法  bigFile 用了LOCAL,mergeFile也只能用用了LOCAL
	 * 		LOCAL:本地方式存储，指单机版本下可以使用，根据 本地文件配置 d:/uploadshp 可以上传到该目录下，并且路径为当日日期分文件夹
	 *		下载方式 就是以  WebResourceConfig 配置类 规定的一样 http://127.0.0.1:9200/api-file/statics/2020-06-28/06B323130BF34AAB88936B8918D90164.avi
	 *		根据网关地址读取 statics 文件下的文件
	 *	（特别注意，该模式下只支持单台服务器，多台服务器会有问题，因为分片会在不太服务器中，暂时无法合并多台服务器的文件，有折中的方法，做共享文件夹，这样成本太大，不建议，如果多台服务器，推荐oss存储或者分布式文件存储）
	 *
	 * 		FASTDFS:分布式文件存储，即分布式系统常用的文件存储方式，适合多台服务器，逻辑是将各个分片存入FASTDFS 的存储目录中，然后在合并方法中把文件 downloadFile 下载到本地进行合并并保存文件
	 * 		最终才是一个文件提供给用户，这里有个问题，就是操作的任务耗时太久，如果经过nginx的有可能被超时返回，建议合并方法可以做异步请求，直接丢到后台任务进行最终通过消息的方式提醒用户即可
	 *
	 *		FASTDFS:七牛OSS上传和FASTDFS类似一样的逻辑,也是适合多台服务器往OSS服务器上传文件，然后在合并方法中把文件保存好完整文件在上传到OSS
	 *
	 *		ALIYUN:暂时没有申请key,没有实现逻辑和七牛OSS一样
	 */
	/**
	 * 上传大文件
	 * @param file
	 * @param chunks
	 */
	@PostMapping(value = "/files-anon/bigFile")
//	@ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR,reason="server error")
	public Result bigFile( String guid, Integer chunk, MultipartFile file, Integer chunks){
//		try {
//            fileServiceFactory.getFileService(FileType.LOCAL.toString()).chunk(guid,chunk,file,chunks,localFilePath);
//            return Result.succeed("操作成功");
//        }catch (Exception ex){
//            return Result.failed("操作失败");
//        }
		return null;
	}


	/**
	 * 合并文件
	 * @param mergeFileDTO
	 */
	@RequestMapping(value = "/files-anon/merge",method =RequestMethod.POST )
	public Result mergeFile(@RequestBody MergeFileDTO mergeFileDTO){
//		try {
//			return Result.succeed(fileServiceFactory.getFileService(FileType.LOCAL.toString()).merge(mergeFileDTO.getGuid(),mergeFileDTO.getFileName(),localFilePath),"操作成功");
//		}catch (Exception ex){
//			return Result.failed("操作失败");
//		}
		return null;
	}


	/**
	 * 上传失败
	 * @param mergeFileDTO
	 * @return
	 */
	@RequestMapping(value = "/files-anon/uploadError",method =RequestMethod.POST )
	public Result uploadError(@RequestBody MergeFileDTO mergeFileDTO){
//		try {
//			//使用默认的 FileService
//			fileServiceFactory.getFileService(null).uploadError(mergeFileDTO.getGuid(),mergeFileDTO.getFileName(),localFilePath);
//			return Result.succeed("操作成功");
//		}catch (Exception ex){
//			return Result.failed("操作失败");
//		}
		return null;
	}



}
