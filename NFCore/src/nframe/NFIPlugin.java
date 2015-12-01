/**
 * 
 */
package nframe;

/**
 * @author Xiong
 *
 */
public abstract class NFIPlugin extends NFILogicModule {
	/**
	 * 安装自身
	 */
	public abstract void install();
	
	/**
	 * 反安装自身
	 */
	public abstract void uninstall();
}
