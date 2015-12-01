/**
 * 
 */
package nframe.server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.NFIPlugin;
import nframe.server.NFDynLib;
import nframe.server.NFPluginManager;

/**
 * @author Xiong
 * 测试动态加载插件
 */
public class DynLib extends NFIPlugin {
	private int step = 0;

	@Test
	public void test() throws Exception {
		NFDynLib dynLib = new NFDynLib("nf-dyn.jar", new NFPluginManager());
		dynLib.install("nframe.server.test.DynLib");
		DynLib plugin = (DynLib) dynLib.getPlugin();
		assertTrue(plugin.getStep() == 1);
		dynLib.uninstall();
		assertTrue(plugin.getStep() == 2);
	}
	
	public int getStep(){
		return step;
	}

	@Override
	public void install() {
		++step;
	}

	@Override
	public void uninstall() {
		++step;
	}

	@Override
	public void afterInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeShut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shut() {
		// TODO Auto-generated method stub
		
	}

}
