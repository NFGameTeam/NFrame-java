package cn.yeegro.nframe.components.storage.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.yeegro.nframe.common.exception.BadRequestException;
import cn.yeegro.nframe.components.storage.model.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 文件工具类
*/
@Slf4j
public class FileUtil {

	public static FileInfo getFileInfo(MultipartFile file) throws Exception {
		String md5 = fileMd5(file.getInputStream());

		FileInfo fileInfo = new FileInfo();
		fileInfo.setMd5(md5);// 将文件的md5设置为文件表的id
		fileInfo.setName(file.getOriginalFilename());
		fileInfo.setContentType(file.getContentType());
		fileInfo.setIsImg(fileInfo.getContentType().startsWith("image/"));
		fileInfo.setSize(file.getSize());

		return fileInfo;
	}

	/**
	 * 文件的md5
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String fileMd5(InputStream inputStream) {
		try {
			return DigestUtils.md5Hex(inputStream);
		} catch (IOException e) {
			log.error("FileUtil->fileMd5:{}" ,e.getMessage());
		}

		return null;
	}

	public static String saveFile(MultipartFile file, String path) {
		try {
			File targetFile = new File(path);
			if (targetFile.exists()) {
				return path;
			}

			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			file.transferTo(targetFile);

			return path;
		} catch (Exception e) {
			log.error("FileUtil->saveFile:{}" ,e.getMessage());
		}

		return null;
	}

	public static String saveBigFile(String guid ,File parentFileDir, File destTempFile) {
		try {
			if(parentFileDir.isDirectory()){
				if(!destTempFile.exists()){
					//先得到文件的上级目录，并创建上级目录，在创建文件,
					destTempFile.getParentFile().mkdir();
					try {
						//创建文件
						destTempFile.createNewFile(); //上级目录没有创建，这里会报错
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				log.info("length:{} ",parentFileDir.listFiles().length);

				for (int i = 0; i < parentFileDir.listFiles().length; i++) {
					File partFile = new File(parentFileDir, guid + "_" + i + ".part");
					FileOutputStream destTempfos = new FileOutputStream(destTempFile, true);
					//遍历"所有分片文件"到"最终文件"中
					FileUtils.copyFile(partFile, destTempfos);
					destTempfos.close();
				}
			}
		} catch (Exception e) {
			log.error("FileUtil->saveBigFile:{}" ,e.getMessage());
		}

		return null;
	}


	public static boolean deleteFile(String pathname) {
		File file = new File(pathname);
		if (file.exists()) {
			boolean flag = file.delete();

			if (flag) {
				File[] files = file.getParentFile().listFiles();
				if (files == null || files.length == 0) {
					file.getParentFile().delete();
				}
			}

			return flag;
		}

		return false;
	}

	//byte数组写到到硬盘上
	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);


			if (!dir.exists() ) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
			log.info("byte2File -》》 成功!!!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从网络Url中下载文件
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadByUrl(String urlStr,String savePath,String fileName){
		InputStream inputStream = null;
		FileOutputStream fos = null;
		try{
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			//设置超时间为3秒
			conn.setConnectTimeout(3*1000);

			//得到输入流
			inputStream = conn.getInputStream();
			//获取自己数组
			byte[] getData = readInputStream(inputStream);

			//文件保存位置
			File saveDir = new File(savePath);
			if(!saveDir.exists()){
				saveDir.mkdir();
			}
			File file = new File(saveDir + File.separator + fileName);
			fos = new FileOutputStream(file);
			fos.write(getData);

			log.info("info:"+url+" download success");
		}catch (IOException e) {
			e.printStackTrace();
			log.info("Unexpected code ");
		}finally {
			try {
				if(fos!=null){
					fos.close();
				}
				if(inputStream!=null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * 定义GB的计算常量
	 */
	private static final int GB = 1024 * 1024 * 1024;
	/**
	 * 定义MB的计算常量
	 */
	private static final int MB = 1024 * 1024;
	/**
	 * 定义KB的计算常量
	 */
	private static final int KB = 1024;

	/**
	 * 格式化小数
	 */
	private static final DecimalFormat DF = new DecimalFormat("0.00");

	/**
	 * MultipartFile转File
	 */
	public static File toFile(MultipartFile multipartFile){
		// 获取文件名
		String fileName = multipartFile.getOriginalFilename();
		// 获取文件后缀
		String prefix="."+getExtensionName(fileName);
		File file = null;
		try {
			// 用uuid作为文件名，防止生成的临时文件重复
			file = File.createTempFile(IdUtil.simpleUUID(), prefix);
			// MultipartFile to File
			multipartFile.transferTo(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 获取文件扩展名，不带 .
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * Java文件操作 获取不带扩展名的文件名
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	/**
	 * 文件大小转换
	 */
	public static String getSize(long size){
		String resultSize;
		if (size / GB >= 1) {
			//如果当前Byte的值大于等于1GB
			resultSize = DF.format(size / (float) GB) + "GB   ";
		} else if (size / MB >= 1) {
			//如果当前Byte的值大于等于1MB
			resultSize = DF.format(size / (float) MB) + "MB   ";
		} else if (size / KB >= 1) {
			//如果当前Byte的值大于等于1KB
			resultSize = DF.format(size / (float) KB) + "KB   ";
		} else {
			resultSize = size + "B   ";
		}
		return resultSize;
	}

	/**
	 * 将文件名解析成文件的上传路径
	 */
	public static File upload(MultipartFile file, String filePath) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
		String name = getFileNameNoEx(file.getOriginalFilename());
		String suffix = getExtensionName(file.getOriginalFilename());
		String nowStr = "-" + format.format(date);
		try {
			String fileName = name + nowStr + "." + suffix;
			String path = filePath + fileName;
			// getCanonicalFile 可解析正确各种路径
			File dest = new File(path).getCanonicalFile();
			// 检测是否存在目录
			if (!dest.getParentFile().exists()) {
				if (!dest.getParentFile().mkdirs()) {
					System.out.println("was not successful.");
				}
			}
			// 文件写入
			file.transferTo(dest);
			return dest;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 导出excel
	 */
	public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
		String tempPath =System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
		File file = new File(tempPath);
		BigExcelWriter writer= ExcelUtil.getBigWriter(file);
		// 一次性写出内容，使用默认样式，强制输出标题
		writer.write(list, true);
		//response为HttpServletResponse对象
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		//test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
		response.setHeader("Content-Disposition","attachment;filename=file.xlsx");
		ServletOutputStream out=response.getOutputStream();
		// 终止后删除临时文件
		file.deleteOnExit();
		writer.flush(out, true);
		//此处记得关闭输出Servlet流
		IoUtil.close(out);
	}

	public static String getFileType(String type) {
		String documents = "txt doc pdf ppt pps xlsx xls docx";
		String music = "mp3 wav wma mpa ram ra aac aif m4a";
		String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
		String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
		if(image.contains(type)){
			return "图片";
		} else if(documents.contains(type)){
			return "文档";
		} else if(music.contains(type)){
			return "音乐";
		} else if(video.contains(type)){
			return "视频";
		} else {
			return "其他";
		}
	}

	public static void checkSize(long maxSize, long size) {
		// 1M
		int len = 1024 * 1024;
		if(size > (maxSize * len)){
			throw new BadRequestException("文件超出规定大小");
		}
	}

	/**
	 * 判断两个文件是否相同
	 */
	public static boolean check(File file1, File file2) {
		String img1Md5 = getMd5(file1);
		String img2Md5 = getMd5(file2);
		return img1Md5.equals(img2Md5);
	}

	/**
	 * 判断两个文件是否相同
	 */
	public static boolean check(String file1Md5, String file2Md5) {
		return file1Md5.equals(file2Md5);
	}

	private static byte[] getByte(File file) {
		// 得到文件长度
		byte[] b = new byte[(int) file.length()];
		try {
			InputStream in = new FileInputStream(file);
			try {
				System.out.println(in.read(b));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return b;
	}

	private static String getMd5(byte[] bytes) {
		// 16进制字符
		char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(bytes);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			// 移位 输出字符串
			for (byte byte0 : md) {
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 下载文件
	 * @param request /
	 * @param response /
	 * @param file /
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, File file, boolean deleteOnExit){
		response.setCharacterEncoding(request.getCharacterEncoding());
		response.setContentType("application/octet-stream");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
			IOUtils.copy(fis,response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
					if(deleteOnExit){
						file.deleteOnExit();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getMd5(File file) {
		return getMd5(getByte(file));
	}

}
