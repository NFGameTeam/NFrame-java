/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang 框架核心,基础元素类
 */

public class NFElement extends NFIElement {

	NFIPropertyManager propertyManager;
	// NFIRecordManager recordManager;

	@Override
	public NFIPropertyManager getPropertyManager() {
		return this.propertyManager;
	}

	/*
	 * @Override public NFIRecordManager getRecordManager() { return
	 * this.recordManager; }
	 */

}
