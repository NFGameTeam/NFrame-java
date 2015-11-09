/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 属性回掉接口
 */
public interface NFIPropertyHandler {
	public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar);
}
