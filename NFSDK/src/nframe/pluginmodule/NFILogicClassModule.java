package nframe.pluginmodule;

import java.util.function.Function;

import nframe.core.FunctionParam;
import nframe.core.NFDefine;
import nframe.core.NFDefine.CLASS_OBJECT_EVENT;
import nframe.core.NFIDataList;

/**
 * @author zhiyu.zhao
 * @Description:
 * 
 */
public abstract class NFILogicClassModule extends NFILogicModule
{
	public abstract boolean load();

	public abstract boolean save();

	public abstract boolean clear();

	public abstract boolean addClassCallBack(String strClassName, Function<FunctionParam, Integer> function);

	public abstract boolean doEvent(NFGUID objectID, String strClassName, CLASS_OBJECT_EVENT eClassEvent, NFIDataList valueList);
}