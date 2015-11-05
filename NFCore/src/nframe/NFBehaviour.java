/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 所有类的基类，所有的行为组件等，都从这里来
 */
public abstract class NFBehaviour {
	
	/** 对象唯一标识 */
	private NFIdent oid;
	
	public abstract void init();

	public abstract void afterInit();

	public abstract void beforeShut();

	public abstract void shut();

	public abstract void execute();
	
	/**
	 * 设置oid
	 * @param oid
	 */
	public void setId(NFIdent oid) {
		this.oid = oid;
	}
	
	/**
	 * 获取id，如果id是null，自动构建一个空的id
	 * @return
	 */
	public NFIdent getId() {
		if (oid == null){
			oid = new NFIdent();
		}
		return oid;
	}
}
