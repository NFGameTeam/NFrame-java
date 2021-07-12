package cn.yeegro.nframe.components.storage.model;

/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 仅支持阿里云 oss ,七牛云等
*/
public enum FileType {
//	七牛
	QINIU ,
//	阿里云
	ALIYUN,
	// 本地存储
	LOCAL, 
	//fastdfs存储
	FASTDFS
}
