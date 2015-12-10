/**
 * 
 */
package nframe.server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.server.NFPluginManager;

/**
 * @author Xiong
 * 测试插件管理器
 */
public class PluginManager {

	@Test
	public void test() {
		NFPluginManager.getInstance().install();
		assertTrue("test".equals(NFPluginManager.getInstance().getClassPath()));
		
		NFPluginManager.getInstance().init();
		NFPluginManager.getInstance().afterInit();
		
		NFPluginManager.getInstance().execute();
		
		NFPluginManager.getInstance().beforeShut();
		NFPluginManager.getInstance().shut();
	}

}
