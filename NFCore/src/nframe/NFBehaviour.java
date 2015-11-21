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
	private NFGUID guid;
	
	public abstract void init();

	public abstract void afterInit();

	public abstract void beforeShut();

	public abstract void shut();

	public abstract void execute();
	
	/**
	 * 设置oid
	 * @param guid
	 */
	public void setId(NFGUID guid) {
		this.guid = guid;
	}
	
	/**
	 * 获取id，如果id是null，自动构建一个空的id
	 * @return
	 */
	public NFGUID getId() {
		if (guid == null){
			guid = new NFGUID();
		}
		return guid;
	}
}
