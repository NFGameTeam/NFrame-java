/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 属性回掉接口
 */
public interface NFIPropertyHandler {
	public void handle(NFIdent self, String propName, NFIDataList oldVar, NFIDataList newVar);
}
