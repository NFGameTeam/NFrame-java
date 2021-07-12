/**   
* @Title: NFIComponent
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.module;


import cn.yeegro.nframe.comm.code.api.NFGUID;

public class NFIComponent implements NFIModule {

	public NFIComponent() {

	}

	public NFIComponent(NFGUID self, String strName) {
		mbHasInit = false;
		mbEnable = true;
		mSelf = self;
		mstrName = strName;
	}

	public <T> T Create_New_Instance() {
		NFIComponent pComponent = CreateNew_Instance();
		if (null != pComponent) {

		}
		return (T) pComponent;
	}

	public boolean SetEnable(boolean bEnable) {
		return mbEnable;
	}

	public boolean Enable() {
		return mbEnable;
	}

	public boolean SetHasInit(boolean bEnable) {
		mbHasInit = bEnable;
		return mbHasInit;
	}

	public boolean HasInit() {
		return mbHasInit;
	}

	public NFGUID Self() {
		return new NFGUID();
	}

	public String GetComponentName() {
		return mstrName;
	};

	// for actor
	public int OnASyncEvent(NFGUID self, int event, String arg) {
		return 0;
	}

	protected NFIComponent CreateNew_Instance() {
		return null;
	}

	private boolean mbEnable;
	private boolean mbHasInit;
	private NFGUID mSelf;
	private String mstrName;
	@Override
	public boolean Awake() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Init() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean AfterInit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CheckConfig() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ReadyExecute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Execute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean BeforeShut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Shut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Finalize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean OnReloadPlugin() {
		// TODO Auto-generated method stub
		return false;
	}
}
