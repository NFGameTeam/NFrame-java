/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang 配置插件
 */

public class NFConfigPlugin extends NFIPlugin {
	private NFILogicClassModule logicClassModule;
	private NFIElementInfoModule elementInfoModule;

	@Override
	public void install() {
		logicClassModule = new NFLogicClassModule();
		elementInfoModule = new NFElementInfoModule();
		this.getPluginManager().addModule("nframe.NFElementInfoModule", logicClassModule);
		this.getPluginManager().addModule("nframe.NFLogicClassModule", elementInfoModule);
	}

	/**
	 * 反安装自身
	 */
	@Override
	public void uninstall() {
	}

	@Override
	public void init() {
		logicClassModule.init();
		elementInfoModule.init();
	}

	@Override
	public void afterInit() {
		logicClassModule.afterInit();
		elementInfoModule.afterInit();
	}

	@Override
	public void beforeShut() {
		logicClassModule.beforeShut();
		elementInfoModule.beforeShut();
	}

	@Override
	public void shut() {
		logicClassModule.shut();
		elementInfoModule.shut();
	}

	@Override
	public void execute() {
		logicClassModule.execute();
		elementInfoModule.execute();
	}
}
