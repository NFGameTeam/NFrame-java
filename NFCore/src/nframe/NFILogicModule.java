/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类
 */

public abstract class NFILogicModule extends NFBehaviour {
	
    public NFIPluginManager getMng()
    {
        return mxMng;
    }
    
    public void setMng(NFIPluginManager xMng)
    {
        mxMng = xMng;
    }
    private NFIPluginManager mxMng;
}
