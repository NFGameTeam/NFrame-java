/**   
* @Title: NFGUID
* @Package ${package_name} 
* @Description: 唯一标识
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.api;

public class NFGUID {

	public long nData64;
	public long nHead64;
	
	public NFGUID()
    {
        nData64 = 0;
        nHead64 = 0;
    }

	public NFGUID(long nHeadData, long nData)
    {
        nHead64 = nHeadData;
        nData64 = nData;
    }

	public NFGUID(NFGUID xData)
    {
        nHead64 = xData.nHead64;
        nData64 = xData.nData64;
    }

	public NFGUID oper_eq(NFGUID xData)
    {
        nHead64 = xData.nHead64;
        nData64 = xData.nData64;

        return this;
    }

	public long GetData() 
    {
        return nData64;
    }

	public long GetHead()
    {
        return nHead64;
    }

	public void SetData(int nData)
    {
        nData64 = nData;
    }

	public void SetHead(int nData)
    {
        nHead64 = nData;
    }

	public boolean IsNull()
    {
        return 0 == nData64 && 0 == nHead64;
    }

	public boolean oper_heq (NFGUID id)
    {
        return this.nData64 == id.nData64 && this.nHead64 == id.nHead64;
    }

	public boolean oper_neq (NFGUID id)
    {
        return this.nData64 != id.nData64 || this.nHead64 != id.nHead64;
    }

	public boolean oper_lt(NFGUID id)
    {
        if (this.nHead64 == id.nHead64)
        {
            return this.nData64 < id.nData64;
        }

        return this.nHead64 < id.nHead64;
    }

	public String ToString() 
    {
        return String.valueOf(nHead64) + "-" + String.valueOf(nData64);
    }

	public boolean FromString(String strID)
    {
        int nStrLength = strID.length();
        int nPos = strID.indexOf('-');
        if (nPos == -1)
        {
            return false;
        }

        String strHead = strID.substring(0, nPos);
        String strData = "";
        if (nPos + 1 < nStrLength)
        {
            strData = strID.substring(nPos + 1, nStrLength - nPos);
        }

        try
        {
            nHead64 = Integer.valueOf(strHead);
            nData64 = Integer.valueOf(strData);

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
