package cn.yeegro.nframe.plugin.config;



import cn.yeegro.nframe.comm.code.api.NFComponentManager;
import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.api.NFPropertyManager;
import cn.yeegro.nframe.comm.code.api.NFRecordManager;
import cn.yeegro.nframe.comm.code.functor.CLASS_EVENT_FUNCTOR;
import cn.yeegro.nframe.comm.code.functor.CLASS_OBJECT_EVENT;
import cn.yeegro.nframe.comm.code.module.NFIClass;
import cn.yeegro.nframe.comm.code.module.NFIComponentManager;
import cn.yeegro.nframe.comm.code.module.NFIPropertyManager;
import cn.yeegro.nframe.comm.code.module.NFIRecordManager;
import cn.yeegro.nframe.comm.code.util.NFDataList;

import java.util.*;


public class NFClass implements NFIClass {
	
    private NFIPropertyManager m_pPropertyManager;
    private NFIRecordManager m_pRecordManager;
    private NFIComponentManager m_pComponentManager;

    private NFIClass m_pParentClass;
    private String mstrType;
    private String mstrClassName;
    private String mstrClassInstancePath;

    private List<String> mIdList;

    private List<CLASS_EVENT_FUNCTOR> mxClassEventInfo;
	

	public NFClass(String strClassName)
    {
        m_pParentClass = null;
        mstrClassName = strClassName;

        m_pPropertyManager = new NFPropertyManager(new NFGUID());
        m_pRecordManager = new NFRecordManager(new NFGUID());
        m_pComponentManager = new NFComponentManager(new NFGUID());
        mIdList=new ArrayList<String>();
        mxClassEventInfo=new ArrayList<CLASS_EVENT_FUNCTOR>();
    }
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(String e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends String> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String set(int index, String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(int index, String element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<String> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<String> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NFIPropertyManager GetPropertyManager() {
		// TODO Auto-generated method stub
		return m_pPropertyManager;
	}

	@Override
	public NFIRecordManager GetRecordManager() {
		// TODO Auto-generated method stub
		 return m_pRecordManager;
	}

	@Override
	public NFIComponentManager GetComponentManager() {
		// TODO Auto-generated method stub
		  return m_pComponentManager;
	}

	@Override
	public void SetParent(NFIClass pClass) {
		// TODO Auto-generated method stub
		m_pParentClass = pClass;
	}

	@Override
	public NFIClass GetParent() {
		// TODO Auto-generated method stub
		return m_pParentClass;
	}

	@Override
	public void SetTypeName(String strType) {
		// TODO Auto-generated method stub
		 mstrType = strType;
	}

	@Override
	public String GetTypeName() {
		// TODO Auto-generated method stub
		 return mstrType;
	}

	@Override
	public String GetClassName() {
		return mstrClassName;
	}

	@Override
	public boolean AddId(String strId) {
		mIdList.add(strId);
        return true;
	}

	@Override
	public List<String> GetIDList() {
		// TODO Auto-generated method stub
		return mIdList;
	}

	@Override
	public String GetInstancePath() {
		// TODO Auto-generated method stub
		 return mstrClassInstancePath;
	}

	@Override
	public void SetInstancePath(String strPath) {
		// TODO Auto-generated method stub
		 mstrClassInstancePath = strPath;
	}

	@Override
	public boolean AddClassCallBack(CLASS_EVENT_FUNCTOR cb) {
		// TODO Auto-generated method stub
		 return mxClassEventInfo.add(cb);
	}

	@Override
	public boolean DoEvent(NFGUID objectID, CLASS_OBJECT_EVENT eClassEvent,
			NFDataList valueList) {
		
		for (int i = 0; i < mxClassEventInfo.size(); i++) {
			CLASS_EVENT_FUNCTOR cb=mxClassEventInfo.get(i);
			cb.operator(objectID, mstrClassName, eClassEvent,  valueList);
		}
	    return true;
	}

}
