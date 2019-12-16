package com.noahframe.api.file;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SysPath {

	public static String root=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	
	
	public static File getRelativePath(String strPath)
	{
		File dir=null;
		try {
			dir = new File(root,strPath).getCanonicalFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return dir;
	}

	public static String getResRoot()
	{
		String res_root=null;

		if (System.getProperty("res.Dir")!=null) {
			res_root =System.getProperty("res.Dir");
		}
		else {
			SysProperties pro=new SysProperties("sys");
			String root_rela= pro.getParams("res_dir");
			res_root=SysPath.getRelativePath(root_rela).getPath();
			System.setProperty("res.Dir", res_root);
		}

		return res_root;

	}
	
	public static String getPluginsRoot()
	{
		String plugins_root=null;
		
		if (System.getProperty("plugins.Dir")!=null) {
			plugins_root =System.getProperty("plugins.Dir");
		}
		else {
			SysProperties pro=new SysProperties("sys");
			String root_rela= pro.getParams("plugins_dir");
			plugins_root=SysPath.getRelativePath(root_rela).getPath()+File.separator+"plugins";
			System.setProperty("plugins.Dir", plugins_root);
		}
		
		return plugins_root;
		
	}
	
	public static String getPluginResourceDir(String pluginpath)
	{
		File dir=null;
		try {
			dir = new File(pluginpath,"/classes/").getCanonicalFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dir.getPath();
	}

	public static String getClassRootPath() throws FileNotFoundException {
		String path = ResourceUtils.getURL("classpath:").getPath();
		//=> file:/root/tmp/demo-springboot-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/

		//创建File时会自动处理前缀和jar包路径问题  => /root/tmp
		File rootFile = new File(path);
		if(!rootFile.exists()) {
			rootFile = new File("");
		}
		return rootFile.getAbsolutePath();
	}
	
}
