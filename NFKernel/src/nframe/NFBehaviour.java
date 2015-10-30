/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 所有类的基类，所有的行为组件等，都从这里来
 */
public abstract class NFBehaviour {
	
	public abstract void init();

	public abstract void afterInit();

	public abstract void beforeShut();

	public abstract void shut();

	public abstract void execute();
}
