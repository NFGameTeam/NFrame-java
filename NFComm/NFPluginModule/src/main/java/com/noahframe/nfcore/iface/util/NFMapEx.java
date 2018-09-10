/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class NFMapEx<T,TD> {
	
	public NFMapEx()
	{
		mObjectList=new TreeMap<T, TD>();
	}
	
	public boolean ExistElement(T name)
	{
		return mObjectList.containsKey(name);
	}
	
	public boolean AddElement(T name, TD data)
	{
		boolean itr=mObjectList.containsKey(name);
		if (!itr) {
			mObjectList.put(name, data);
			
			return true;
		}
		
		return false;
	}
	
	public  boolean RemoveElement(T name)
	{
		boolean itr=mObjectList.containsKey(name);
		if (itr) {
			mObjectList.remove(name);
			return true;
		}
		return false;
	}
	
	public TD GetElementNude(T name)
	{
		boolean itr=mObjectList.containsKey(name);
		if (itr) {
			return mObjectList.get(name);
		}else {
			return null;
		}
	}
	
	public TD GetElement(T name)
	{
		boolean itr=mObjectList.containsKey(name);
		if (itr) {
			return mObjectList.get(name);
		}else {
			return null;
		}
	}
	
	public TD FirstNude(T name)
	{
		if (mObjectList.size()<=0) {
			return null;
		}
		mObjectCurIter=mObjectList.entrySet().iterator();
		if (mObjectCurIter.hasNext()) {
			
			Entry<T, TD> iterator=mObjectCurIter.next();
			
			if (iterator!=null) {
				name=iterator.getKey();
				return iterator.getValue();
			}
			
		}else {
			mObjectCurIter=null;
		}

		return null;

	}
	
	public  TD NextNude(T name)
	{
		if (!mObjectCurIter.hasNext()) {
			return null;
		}
		Entry<T, TD> iterator=mObjectCurIter.next();
		if (null!=iterator) {
			name=iterator.getKey();
			return iterator.getValue();
		}
		
		return null;
	}
	
	public TD FirstNude()
	{
		if (mObjectList.size()<=0) {
			return null;
		}
		mObjectCurIter=mObjectList.entrySet().iterator();
		if (mObjectCurIter.hasNext()) {
			
			Entry<T, TD> iterator=mObjectCurIter.next();
			
			if (iterator!=null) {
				return iterator.getValue();
			}
			
		}else {
			mObjectCurIter=null;
		}

		return null;
	}
	
	public  TD NextNude()
	{
		if (!mObjectCurIter.hasNext()) {
			return null;
		}
		Entry<T, TD> iterator=mObjectCurIter.next();
		if (null!=iterator) {
			return iterator.getValue();
		}
		
		return null;
	}
	public TD First()
	{
		if (mObjectList.size()<=0) {
			return null;
		}
		mObjectCurIter=mObjectList.entrySet().iterator();
		if (mObjectCurIter.hasNext()) {
			
			Entry<T, TD> iterator=mObjectCurIter.next();
			
			if (iterator!=null) {
				return iterator.getValue();
			}
			
		}else {
			mObjectCurIter=null;
		}

		return null;
	}
	
	public TD Next()
	{
		if (!mObjectCurIter.hasNext()) {
			return null;
		}
		Entry<T, TD> iterator=mObjectCurIter.next();
		if (null!=iterator) {
			return iterator.getValue();
		}
		
		return null;
	}
	
	public TD First(T name)
	{
		if (mObjectList.size()<=0) {
			return null;
		}
		mObjectCurIter=mObjectList.entrySet().iterator();
		if (mObjectCurIter.hasNext()) {
			
			Entry<T, TD> iterator=mObjectCurIter.next();
			
			if (iterator!=null) {
				name=iterator.getKey();
				return iterator.getValue();
			}
			
		}else {
			mObjectCurIter=null;
		}

		return null;
	}
	
	public TD Next(T name)
	{
		if (!mObjectCurIter.hasNext()) {
			return null;
		}
		Entry<T, TD> iterator=mObjectCurIter.next();
		if (null!=iterator) {
			name=iterator.getKey();
			return iterator.getValue();
		}
		
		return null;
	}
	
	public int Count()
    {
        return (int)mObjectList.size();
    }
	
	public boolean ClearAll()
    {
        mObjectList.clear();
        return true;
    }
	
	
	protected TreeMap<T, TD> mObjectList;
	protected Iterator<Entry<T, TD>> mObjectCurIter;
	
	
	
}
