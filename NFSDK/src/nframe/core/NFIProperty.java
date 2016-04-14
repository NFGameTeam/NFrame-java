package nframe.core;

import java.util.List;
import java.util.function.Function;

import nframe.pluginmodule.NFGUID;

/**
 * 
 * @author zhiyu.zhao
 * @Description:
 *
 */
public interface NFIProperty
{

	public String getName();

	public NFData getValue();

	public abstract void registerCallback(Function<FunctionParam, Integer> function);

	public abstract void setValue(NFData value);

	public abstract void setValue(NFIProperty pProperty);

	public abstract boolean setLong(long value);

	public abstract boolean setDouble(double value);

	public abstract boolean setString(String value);

	public abstract boolean setObject(NFGUID value);

	public abstract long getLong();

	public abstract double getDouble();

	public abstract String getString();

	public abstract NFGUID getObject();

	public abstract boolean geUsed();

	public abstract void setScriptFunction(String strScriptFunction);

	public abstract boolean changed();

	public abstract boolean fromString(String strData);

	public abstract String toString();

	public NFGUID getSelf();

	public void setSelf(NFGUID self);

	public NFData.Type geteType();

	public void seteType(NFData.Type eType);

	public NFData getData();

	public void setData(NFData data);

	public boolean isbPublic();

	public void setbPublic(boolean bPublic);

	public boolean isbPrivate();

	public void setbPrivate(boolean bPrivate);

	public boolean isSave();

	public void setSave(boolean save);

	public boolean isView();

	public void setView(boolean view);

	public int getIndex();

	public void setIndex(int index);

	public List<Function<FunctionParam, Integer>> getPropertyCallback();

	public void setPropertyCallback(List<Function<FunctionParam, Integer>> propertyCallback);

	public void setName(String name);

}
