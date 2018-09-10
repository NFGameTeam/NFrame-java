/**   
* @Title: NFDATA_TYPE
* @Package ${package_name} 
* @Description: 基础数据类型
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.util;

public enum NFDATA_TYPE {

	TDATA_UNKNOWN(0), 
	TDATA_INT(1), 
	TDATA_FLOAT(2), 
	TDATA_STRING(3), 
	TDATA_OBJECT(4), 
	TDATA_VECTOR2(5), 
	TDATA_VECTOR3(6), 
	TDATA_MAX(7);

	private int _value;

	private NFDATA_TYPE(int value) {
		_value = value;
	}

	public int value() {
		return _value;
	}
	public static NFDATA_TYPE get(int ntype)
	{
		for (int i = 0; i < NFDATA_TYPE.values().length; i++) {
			NFDATA_TYPE val=NFDATA_TYPE.values()[i];
			if (val.value()==ntype) {
				return val;
			}
		}
		return null;
	}
}
