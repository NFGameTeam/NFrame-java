/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 属性回掉接口
 */
public interface NFIPropertyHandler {
	public void handle(NFGUID guid, String propName, NFIData oldVar, NFIData newVar);
}
