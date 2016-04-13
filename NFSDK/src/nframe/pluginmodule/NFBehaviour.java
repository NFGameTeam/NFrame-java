package nframe.pluginmodule;

/**
 * 
 * @author zhiyu.zhao
 * @Description: 
 *
 */
public abstract class NFBehaviour
{
	public boolean init()
	{
		return true;
	}

	public boolean afterInit()
	{
		return true;
	}

	public boolean checkConfig()
	{
		return true;
	}

	public boolean execute()
	{
		return true;
	}

	public boolean beforeShut()
	{
		return true;
	}

	public boolean shut()
	{
		return true;
	}

}
