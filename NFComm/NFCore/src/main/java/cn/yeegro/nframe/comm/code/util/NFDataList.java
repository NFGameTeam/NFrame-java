/**   
* @Title: NFDataList
* @Package ${package_name} 
* @Description: 基础数据列表对象
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;
import org.apache.commons.lang.StringUtils;



public class NFDataList {

	
	public NFDataList()
	{
		mnUseSize=0;
		mvList=new ArrayList<NFData>();
		for (int i = 0; i < STACK_SIZE; i++) {
			mvList.add(new NFData());
		}
	}
	
	public String StringValEx(int index){

		if (ValidIndex(index))
		{
			String strData;

			NFDATA_TYPE eType = Type(index);
			switch (eType)
			{
			case TDATA_INT:
				strData = String.valueOf(Int(index));
				break;

			case TDATA_FLOAT:
				strData = String.valueOf(Float(index));
				break;

			case TDATA_STRING:
				strData = String(index);
				break;

			case TDATA_OBJECT:
				strData = Object(index).ToString();
				break;

			case TDATA_VECTOR2:
				strData = Vector2(index).ToString();
				break;

			case TDATA_VECTOR3:
				strData = Vector3(index).ToString();
				break;

			default:
				strData = null;
				break;
			}
		}

		return null;
	}
	
	public boolean ToString(String str, String strSplit)
	{
		for (int i = 0; i < GetCount(); ++i)
		{
			String strVal = StringValEx(i);
			str += strVal;
			str += strSplit;
		}

		String strTempSplit = new String(strSplit);
		int nPos = str.lastIndexOf(strSplit);
		if (nPos == str.length() - strTempSplit.length())
		{
			str = str.substring(0, nPos);
		}

		return true;
	}
	
	public NFData GetStack(int index)
	{
		if (index<mvList.size()) {
			return mvList.get(index);
		}
		return null;
	}
	
	public boolean Concat(NFDataList src)
	{
		InnerAppendEx(src, 0, src.GetCount());
		return true;
	}
	
	public boolean Append(NFDataList src)
	{
		return Append(src, 0, src.GetCount());
	}
	
	public boolean Append(NFDataList src, int start, int count)
	{
		if (start >= src.GetCount())
		{
			return false;
		}

		int end = start + count;

		if (end > src.GetCount())
		{
			return false;
		}

		InnerAppendEx(src, start, end);

		return true;
	}
	
	public boolean Append(NFData xData)
	{
		switch (xData.GetType())
		{
		case TDATA_INT:
			AddInt(xData.GetInt());
			break;
		case TDATA_FLOAT:
			AddFloat(xData.GetFloat());
			break;
		case TDATA_OBJECT:
			AddObject(xData.GetObject());
			break;
		case TDATA_STRING:
			AddString(xData.GetString());
			break;
		case TDATA_VECTOR2:
			AddVector2(xData.GetVector2());
			break;
		case TDATA_VECTOR3:
			AddVector3(xData.GetVector3());
			break;
		default:
			break;
		}

		return true;
	}
	
	public void Clear()
	{
		mnUseSize = 0;

		if (mvList.size() > STACK_SIZE)
		{
			for (int i = 0; i < STACK_SIZE; ++i)
			{
				mvList.get(i).Reset();
			}

			mvList.clear();
		}
	}
    
	public boolean IsEmpty()
	{
		return (0 == mnUseSize);
	}
	
	public int GetCount()
	{
		return mnUseSize;
	}
	
	public NFDATA_TYPE Type(int index)
	{
		if (!ValidIndex(index))
		{
			return NFDATA_TYPE.TDATA_UNKNOWN;
		}

		if (index < STACK_SIZE)
		{
			return mvList.get(index).GetType();
		}
		else
		{
			NFData pData = GetStack(index);
			if (pData != null)
			{
				return pData.GetType();
			}
		}

		return NFDATA_TYPE.TDATA_UNKNOWN;
	}
    
	public boolean TypeEx(int nType,Object...objects)
	{

		boolean bRet = true;

		if (NFDATA_TYPE.TDATA_UNKNOWN.value() == nType)
		{
			bRet = false;
			return bRet;
		}

		
		for (int i = 0; i < NFDATA_TYPE.values().length; i++) {
			NFDATA_TYPE pareType=NFDATA_TYPE.values()[i];
			NFDATA_TYPE varType = Type(i);
			if (varType != pareType)
			{
				bRet = false;
				break;
			}

		}
		
		return bRet;
	}
    
	public boolean Split( String str,  String strSplit)
	{
		
		Clear();
		if (str.isEmpty())
		{
			return true;
		}
		
		String[] strs= StringUtils.split(str,strSplit);

		for (int i = 0; i < strs.length; i++)
		{
				Add(strs[i]);
		}

		return true;
	}
	
	
	public boolean Add(int value)
	{
		if (GetCount()==mvList.size()) {
			AddStatck();
		}
		NFData var=GetStack(GetCount());
		if (null!=var) {
			var.SetInt(value);
			mnUseSize++;
			return true;
		}
		return false;
	}
	public boolean Add(double value)
	{
		if (GetCount() == mvList.size())
		{
			AddStatck();
		}

		NFData var = GetStack(GetCount());
		if (var != null)
		{
			var.SetFloat(value);
			mnUseSize++;

			return true;
		}

		return false;
	}

	public boolean Add(String value)
	{
		if (GetCount() == mvList.size())
		{
			AddStatck();
		}

		NFData var = GetStack(GetCount());
		if (var != null)
		{
			var.SetString(value);
			mnUseSize++;

			return true;
		}

		return false;
	}

	public boolean Add(NFGUID value)
	{
		if (GetCount() == mvList.size())
		{
			AddStatck();
		}

		NFData var = GetStack(GetCount());
		if (var != null)
		{
			var.SetObject(value);
			mnUseSize++;

			return true;
		}

		return false;
	}

	public boolean Add(NFVector2 value)
	{
		if (GetCount() == mvList.size())
		{
			AddStatck();
		}

		NFData var = GetStack(GetCount());
		if (var != null)
		{
			var.SetVector2(value);
			mnUseSize++;

			return true;
		}

		return false;
	}

	public boolean Add(NFVector3 value)
	{
		if (GetCount() == mvList.size())
		{
			AddStatck();
		}

		NFData var = GetStack(GetCount());
		if (var != null)
		{
			var.SetVector3(value);
			mnUseSize++;

			return true;
		}

		return false;
	}
	
	public boolean Set( int index,  int value)
	{
		if (ValidIndex(index) && Type(index) == NFDATA_TYPE.TDATA_INT)
		{
			NFData var = GetStack(index);
			if (var != null)
			{
				var.SetInt(value);

				return true;
			}
		}

		return false;
	}

	public boolean Set( int index,  double value)
	{
		if (ValidIndex(index) && Type(index) == NFDATA_TYPE.TDATA_FLOAT)
		{
			NFData var = GetStack(index);
			if (var != null)
			{
				var.SetFloat(value);

				return true;
			}
		}

		return false;
	}

	public boolean Set( int index,  String value)
	{
		if (ValidIndex(index) && Type(index) == NFDATA_TYPE.TDATA_STRING)
		{
			NFData var = GetStack(index);
			if (var != null)
			{
				var.SetString(value);

				return true;
			}
		}

		return false;
	}

	public boolean Set( int index,  NFGUID value)
	{
		if (ValidIndex(index) && Type(index) == NFDATA_TYPE.TDATA_OBJECT)
		{
			NFData var = GetStack(index);
			if (var != null)
			{
				var.SetObject(value);

				return true;
			}
		}

		return false;
	}

	public boolean Set( int index,  NFVector2 value)
	{
		if (ValidIndex(index) && Type(index) == NFDATA_TYPE.TDATA_VECTOR2)
		{
			NFData var = GetStack(index);
			if (var != null)
			{
				var.SetVector2(value);

				return true;
			}
		}

		return false;
	}

	public boolean Set( int index,  NFVector3 value)
	{
		if (ValidIndex(index) && Type(index) == NFDATA_TYPE.TDATA_VECTOR3)
		{
			NFData var = GetStack(index);
			if (var != null)
			{
				var.SetVector3(value);

				return true;
			}
		}

		return false;
	}

    
	public int Int( int index) 
	{
		if (ValidIndex(index))
		{
			if (Type(index) == NFDATA_TYPE.TDATA_INT)
			{
				 NFData var = GetStack(index);
				return var.GetInt();
			}
		}

		return 0;
	}

	public double Float( int index) 
	{
		if (ValidIndex(index))
		{
			 NFData var = mvList.get(index);
			if (var!=null && NFDATA_TYPE.TDATA_FLOAT == var.GetType())
			{
				return var.GetFloat();
			}
		}

		return 0.0f;
	}

	public  String String( int index) 
	{
		if (ValidIndex(index))
		{
			 NFData var = mvList.get(index);
			if (var!=null && NFDATA_TYPE.TDATA_STRING == var.GetType())
			{
				return var.GetString();
			}
		}

		return null;
	}

	public  NFGUID Object( int index)
	{
		if (ValidIndex(index))
		{
			NFDATA_TYPE type = Type(index);
			if (NFDATA_TYPE.TDATA_OBJECT == type)
			{
				NFData var = GetStack(index);
				if (var != null)
				{
					return var.GetObject();
				}
			}
		}

		return null;
	}

	public  NFVector2 Vector2( int index)
	{
		if (ValidIndex(index))
		{
			NFDATA_TYPE type = Type(index);
			if (NFDATA_TYPE.TDATA_VECTOR2 == type)
			{
				NFData var = GetStack(index);
				if (var != null)
				{
					return var.GetVector2();
				}
			}
		}

		return null;
	}

	public NFVector3 Vector3(int index)
	{
		if (ValidIndex(index))
		{
			NFDATA_TYPE type = Type(index);
			if (NFDATA_TYPE.TDATA_VECTOR3 == type)
			{
				NFData var = GetStack(index);
				if (var != null)
				{
					return var.GetVector3();
				}
			}
		}

		return null;
	}
	
	public boolean AddInt(int value)
	{
		return Add(value);
	}
	
	public boolean AddFloat( double value)
	    {
	        return Add(value);
	    }
	public boolean AddString( String value)
	    {
	        return Add(value);
	    }
	public boolean AddStringFromChar(String value)
	    {
	        return Add(value);
	    }
	    public boolean AddObject( NFGUID value)
	    {
	        return Add(value);
	    }
	    public boolean AddVector2( NFVector2 value)
		{
			return Add(value);
		}
	    public boolean AddVector3( NFVector3 value)
		{
			return Add(value);
		}

	    public boolean SetInt( int index,  int value)
	    {
	        return Set(index, value);
	    }
	    public boolean SetFloat( int index,  double value)
	    {
	        return Set(index, value);
	    }
	    public boolean SetString( int index,  String value)
	    {
	        return Set(index, value);
	    }
	    public boolean SetObject( int index,  NFGUID value)
	    {
	        return Set(index, value);
	    }
	    public boolean SetVector2( int index,  NFVector2 value)
		{
			return Set(index, value);
		}
	    public boolean SetVector3( int index,  NFVector3 value)
		{
			return Set(index, value);
		}



	    public boolean Compare( int nPos,  NFDataList src)
	    {
	        if (src.GetCount() > nPos
	            && GetCount() > nPos
	            && src.Type(nPos) == Type(nPos))
	        {
	            switch (src.Type(nPos))
	            {
	                case TDATA_INT:
	                    return Int(nPos) == src.Int(nPos);
	                case TDATA_FLOAT:
	                    return Math.abs(Float(nPos) - src.Float(nPos)) < 0.001f;

	                case TDATA_STRING:
	                    return String(nPos) == src.String(nPos);

	                case TDATA_OBJECT:
	                    return Object(nPos) == src.Object(nPos);

					case TDATA_VECTOR2:
						return Vector2(nPos) == src.Vector2(nPos);

					case TDATA_VECTOR3:
						return Vector3(nPos) == src.Vector3(nPos);
	                default:
	                    return false;
	            }
	        }

	        return false;
	    }


	    public boolean oper_heq( NFDataList src)
	    {
	        if (src.GetCount() == GetCount())
	        {
	            for (int i = 0; i < GetCount(); i++)
	            {
	                if (!Compare(i, src))
	                {
	                    return false;
	                }
	            }

	            return true;
	        }

	        return false;
	    }

	    public boolean oper_ne( NFDataList src)
	    {
	        return !(this.oper_heq(src));
	    }
	    public NFDataList oper_push( double value)
	    {
	        Add(value);
	        return this;
	    }
//	    public NFDataList oper_push( char[] value)
//	    {
//	        Add(value);
//	        return this;
//	    }
	    public NFDataList oper_push( String value)
	    {
	        Add(value);
	        return this;
	    }

	    public NFDataList oper_push( int value)
	    {
	        Add(value);
	        return this;
	    }
	    public NFDataList oper_push( NFGUID value)
	    {
	        Add(value);
	        return this;
	    }
		public NFDataList oper_push( NFVector2 value)
		{
			Add(value);
			return this;
		}
		public NFDataList oper_push( NFVector3 value)
		{
			Add(value);
			return this;
		}
	    public NFDataList oper_push( NFDataList value)
	    {
	        Concat(value);
	        return this;
	    }
	
	    protected boolean ValidIndex(int index) 
		{
			return (index < GetCount()) && (index >= 0);
		}
	protected void AddStatck()
	{
		for (int i = 0; i < STACK_SIZE; i++) {
			NFData pData=new NFData();
			mvList.add(pData);
		}
	}
	void InnerAppendEx( NFDataList src,  int start,  int end)
	{
		for (int i = start; i < end; ++i)
		{
			NFDATA_TYPE vType = src.Type(i);
			switch (vType)
			{
			case TDATA_INT:
				AddInt(src.Int(i));
				break;
			case TDATA_FLOAT:
				AddFloat(src.Float(i));
				break;
			case TDATA_STRING:
				AddString(src.String(i));
				break;
			case TDATA_OBJECT:
				AddObject(src.Object(i));
				break;
			case TDATA_VECTOR2:
				AddVector2(src.Vector2(i));
				break;
			case TDATA_VECTOR3:
				AddVector3(src.Vector3(i));
				break;
			default:
				break;
			}
		}
	}
	
	
	public int STACK_SIZE=8;
	protected int mnUseSize;
	protected List<NFData> mvList;
	protected Map<String, NFData > mxMap;
}
