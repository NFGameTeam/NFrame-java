/**
 * @Title: NFData
 * @Package ${package_name}
 * @Description: 数据基础对象
 * @author zoecee yideal_formula@126.com
 * @date 2017.7.6
 * @version V1.0
 */
package cn.yeegro.nframe.comm.code.util;


import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.math.NFVector2;
import cn.yeegro.nframe.comm.code.math.NFVector3;

public class NFData {

	public NFData()
	{
		nType=NFDATA_TYPE.TDATA_UNKNOWN;
	}
	public NFData(NFDATA_TYPE eType)
	{
		nType=eType;
	}
	public NFData(NFData value)
	{
		nType=value.nType;
		variantData = value.variantData;
	}

	public boolean oper_heq(NFData src)
	{
		//&& src.variantData == variantData
		if (src.GetType() == GetType())
		{
			switch (GetType())
			{
				case TDATA_INT:
				{
					if (src.GetInt() == GetInt())
					{
						return true;
					}
				}
				break;
				case TDATA_FLOAT:
				{
					double fValue = GetFloat() - src.GetFloat();
					if (fValue < 0.001  && fValue > -0.001)
					{
						return true;
					}
				}
				break;
				case TDATA_STRING:
				{
					if (src.GetString() == GetString())
					{
						return true;
					}
				}
				break;
				case TDATA_OBJECT:
				{
					if (src.GetObject() == GetObject())
					{
						return true;
					}
				}
				break;
				case TDATA_VECTOR2:
				{
					if (src.GetVector2() == GetVector2())
					{
						return true;
					}
				}
				break;
				case TDATA_VECTOR3:
				{
					if (src.GetVector3() == GetVector3())
					{
						return true;
					}
				}
				break;
				default:
					break;
			}
		}

		return false;
	}


	public void Reset()
	{
		nType = NFDATA_TYPE.TDATA_UNKNOWN;
	}

	public boolean IsNullValue()
	{
		boolean bChanged = false;

		switch (GetType())
		{
			case TDATA_INT:
			{
				if (0 != GetInt())
				{
					bChanged = true;
				}
			}
			break;
			case TDATA_FLOAT:
			{
				double fValue = GetFloat();
				if (fValue > 0.001 || fValue < -0.001)
				{
					bChanged = true;
				}
			}
			break;
			case TDATA_STRING:
			{
				String strData = GetString();
				if (!strData.isEmpty())
				{
					bChanged = true;
				}
			}
			break;
			case TDATA_OBJECT:
			{
				if (!GetObject().IsNull())
				{
					bChanged = true;
				}
			}
			break;
			case TDATA_VECTOR2:
			{
				if (!GetVector2().IsZero())
				{
					bChanged = true;
				}
			}
			break;
			case TDATA_VECTOR3:
			{
				if (!GetVector3().IsZero())
				{
					bChanged = true;
				}
			}
			break;
			default:
				break;
		}

		return !bChanged;
	}

	public NFDATA_TYPE GetType()
	{
		return nType;
	}


	public void SetInt(int var)
	{
		if (nType==NFDATA_TYPE.TDATA_INT || NFDATA_TYPE.TDATA_UNKNOWN==nType) {
			nType=NFDATA_TYPE.TDATA_INT;
			variantData=var;
		}
	}

	public void SetFloat( double var)
	{
		if (nType == NFDATA_TYPE.TDATA_FLOAT || NFDATA_TYPE.TDATA_UNKNOWN == nType)
		{
			nType = NFDATA_TYPE.TDATA_FLOAT;
			variantData = (double)var;
		}
	}

	public void SetString(String var)
	{
		if (nType == NFDATA_TYPE.TDATA_STRING || NFDATA_TYPE.TDATA_UNKNOWN == nType)
		{
			nType = NFDATA_TYPE.TDATA_STRING;
			variantData = (String)var;
		}
	}

	public void SetObject(NFGUID var)
	{
		if (nType == NFDATA_TYPE.TDATA_OBJECT || NFDATA_TYPE.TDATA_UNKNOWN == nType)
		{
			nType = NFDATA_TYPE.TDATA_OBJECT;
			variantData = (NFGUID)var;
		}
	}

	public void SetVector2(NFVector2 var)
	{
		if (nType == NFDATA_TYPE.TDATA_VECTOR2 || NFDATA_TYPE.TDATA_UNKNOWN == nType)
		{
			nType = NFDATA_TYPE.TDATA_VECTOR2;
			variantData = (NFVector2)var;
		}
	}

	public void SetVector3(NFVector3 var)
	{
		if (nType == NFDATA_TYPE.TDATA_VECTOR3 || NFDATA_TYPE.TDATA_UNKNOWN == nType)
		{
			nType = NFDATA_TYPE.TDATA_VECTOR3;
			variantData = (NFVector3)var;
		}
	}

	public int GetInt()
	{
		if (NFDATA_TYPE.TDATA_INT == nType)
		{
			//return boost::get<NFINT64>(variantData);
			return (Integer) variantData;
		}

		return Integer.MIN_VALUE;
	}

	public double GetFloat()
	{
		if (NFDATA_TYPE.TDATA_FLOAT == nType)
		{
			//return boost::get<double>(variantData);
			return (Double) variantData;
		}

		return Double.NaN;
	}
	public String GetString()
	{
		if (NFDATA_TYPE.TDATA_STRING == nType)
		{
			//return boost::get< std::string&>(variantData);
			return (String)variantData;
		}

		return null;
	}

	public char[] GetCharArr()
	{
		if (NFDATA_TYPE.TDATA_STRING == nType)
		{
			//return boost::get< std::string&>(variantData);
			return ((String)variantData).toCharArray();
		}

		return null;
	}

	public NFGUID GetObject()
	{
		if (NFDATA_TYPE.TDATA_OBJECT == nType)
		{
			//return boost::get< NFGUID&>(variantData);
			return (NFGUID)variantData;
		}

		return null;
	}

	public NFVector2 GetVector2()
	{
		if (NFDATA_TYPE.TDATA_VECTOR2 == nType)
		{
			return (NFVector2)variantData;
		}

		return null;
	}

	public NFVector3 GetVector3()
	{
		if (NFDATA_TYPE.TDATA_VECTOR3 == nType)
		{
			return (NFVector3)variantData;
		}

		return null;
	}


	private NFDATA_TYPE nType;

	public Object variantData;
}
