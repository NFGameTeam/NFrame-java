/**   
* @Title: RECORD_EVENT_DATA 
* @Package ${package_name} 
* @Description: 记录事件数据类型
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.functor;

public class RECORD_EVENT_DATA {

	public enum RecordOptype{
		Add(0),
		Del(1),
		Swap(2),
		Create(3),
		Update(4),
		Cleared(5),
		Sort(6),
		Cover(7),
		UNKNOW(8);
		
		  private int _value;

			private RecordOptype(int value) {
				_value = value;
			}

			public int value() {
				return _value;
			}
			public static RecordOptype get(int ntype)
			{
				for (int i = 0; i < RecordOptype.values().length; i++) {
					RecordOptype val=RecordOptype.values()[i];
					if (val.value()==ntype) {
						return val;
					}
				}
				return null;
			}
		
	}
	
	
	public RECORD_EVENT_DATA()
	{
		nOpType = RecordOptype.UNKNOW.value();
		nRow = 0;
		nCol = 0;
	}

	public int nOpType;
	public int nRow;
	public int nCol;
	public String strRecordName;
	
}
