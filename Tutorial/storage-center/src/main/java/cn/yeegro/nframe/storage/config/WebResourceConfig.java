package cn.yeegro.nframe.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 使系统加载jar包外的文件
 */
@Configuration
public class WebResourceConfig  implements WebMvcConfigurer {

	/**
	 * 上传文件存储在本地的根路径
	 */
	@Value("${file.oss.path}")
	private String localFilePath;

	/**
	 * url前缀
	 */
	@Value("${file.oss.prefix}")
	public String localFilePrefix;

	// 注意!!!配置磁盘路径在启动时必须创建文件夹,如D盘下必须有prefix文件夹下.否则启动会报错.
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(localFilePrefix + "/**")
		.addResourceLocations(ResourceUtils.FILE_URL_PREFIX + localFilePath + File.separator);
    }


}
