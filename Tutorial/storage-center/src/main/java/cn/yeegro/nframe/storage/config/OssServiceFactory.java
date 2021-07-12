package cn.yeegro.nframe.storage.config;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.comm.loader.NFPluginManager;
import cn.yeegro.nframe.common.utils.SpringUtils;
import cn.yeegro.nframe.components.storage.iface.NFIFastDFSOssModule;
import cn.yeegro.nframe.components.storage.iface.NFIStorageModule;
import cn.yeegro.nframe.components.storage.model.FileType;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
 * FileService工厂<br>
 * 将各个实现类放入map
*/
@Configuration
public class OssServiceFactory {

	private Map<FileType, NFIStorageModule> map = new HashMap<>();

	private NFIPluginManager pPluginManager;
	private NFIStorageModule m_pAliyunOssModule;
	private NFIStorageModule m_pQiniuOssModule;
	private NFIStorageModule m_pFastDfsOssModule;
	private NFIStorageModule m_pLocalOsssModule;

	OssServiceFactory(ApplicationContext context) {
		// 在初始化AutoConfiguration时会自动传入ApplicationContext
		SpringUtils.setAppContext(context);
		pPluginManager= NFPluginManager.GetSingletonPtr();
		m_pFastDfsOssModule=pPluginManager.FindModule(NFIFastDFSOssModule.class);
	}


	@PostConstruct
	public void init() {
//		map.put(FileType.ALIYUN,  aliyunOssServiceImpl);
//		map.put(FileType.QINIU ,  qiniuOssServiceImpl);
//		map.put(FileType.LOCAL ,  localOssServiceImpl);
		map.put(FileType.FASTDFS ,  m_pFastDfsOssModule);
	}

	public NFIStorageModule getFileModule(String fileType) {
	   if (StringUtils.isBlank(fileType)) {
			return m_pFastDfsOssModule;
		}

		return map.get(FileType.valueOf(fileType));
	}
}
