/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.module;

import java.util.Date;

public class NFPlatform {

	
	public static void NFASSERT(int exp_, String msg_, String file_, String func_)
	{
		String strInfo="Message:";
	    strInfo += msg_ + " don't exist or some warning" + "\n\nFile:" + file_ + "\n Function:" + func_;
	    System.err.println(strInfo);

	}
	
	public static <DTYPE> boolean NF_StrTo(String strValue, DTYPE nValue)
	{
		try {
			nValue=(DTYPE) strValue;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean IsZeroFloat(float fValue)
	{
	    return Math.abs(fValue) <= 0.00001;
	}

	public static boolean IsZeroDouble(double dValue)
	{
	    return Math.abs(dValue) <= 0.00001;
	}
	
	public static long NFGetTime()
	{
		return new Date().getTime();
	}
	
}
