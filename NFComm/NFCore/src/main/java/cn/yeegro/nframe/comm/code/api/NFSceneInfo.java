/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.code.api;


import cn.yeegro.nframe.comm.code.math.NFVector3;
import cn.yeegro.nframe.comm.code.util.NFMapEx;

public class NFSceneInfo extends NFMapEx<Integer, NFSceneGroupInfo> {

	public NFSceneInfo(int nSceneID)
	{
		  mnGroupIndex = -1;
	      mnSceneID = nSceneID;
	      mnWidth = 512;
	}
	
	public NFSceneInfo(int nSceneID, int nWidth)
    {
        mnGroupIndex = -1;
        mnSceneID = nSceneID;
        mnWidth = nWidth;
    }
	
	public  int NewGroupID()
    {
		mnGroupIndex += 1;
		return mnGroupIndex;
    }
	public   int GetWidth()
    {
        return mnWidth;
    }
	
	public  boolean AddObjectToGroup(int nGroupID, NFGUID ident, boolean bPlayer)
    {
        NFSceneGroupInfo pInfo = GetElement(nGroupID);
        if (pInfo != null)
        {
            if (bPlayer)
            {
                return pInfo.mxPlayerList.AddElement(ident, new Integer(0));
            }
            else
            {
            	Integer in;
                return pInfo.mxOtherList.AddElement(ident, new Integer(0));
            }
        }

        return false;
    }
	
	public  boolean RemoveObjectFromGroup(int nGroupID, NFGUID ident, boolean bPlayer)
    {
		NFSceneGroupInfo pInfo = GetElement(nGroupID);
        if (pInfo != null)
        {
            if (bPlayer)
            {
                return pInfo.mxPlayerList.RemoveElement(ident);
            }
            else
            {
                return pInfo.mxOtherList.RemoveElement(ident);
            }
        }

        return false;
    }
	
	public  boolean ExistObjectInGroup( int nGroupID,  NFGUID ident)
	{
		NFSceneGroupInfo pInfo = GetElement(nGroupID);
		if (pInfo != null)
		{
			return pInfo.mxPlayerList.ExistElement(ident) || pInfo.mxOtherList.ExistElement(ident);
		}

		return false;
	}

    public  boolean Execute()
    {
    	NFSceneGroupInfo pGroupInfo = First();
        while (pGroupInfo != null)
        {
            pGroupInfo.Execute();

            pGroupInfo = Next();
        }
        return true;
    }

	public  boolean AddSeedObjectInfo(String strSeedID, String strConfigID, NFVector3 vPos, int nWeight)
	{
		SceneSeedResource pInfo = mtSceneResourceConfig.GetElement(strSeedID);
		if (null!=pInfo)
		{
			pInfo = new SceneSeedResource();
			pInfo.strSeedID = strSeedID;
			pInfo.strConfigID = strConfigID;
			pInfo.vSeedPos = vPos;
			pInfo.nWeight = nWeight;
			return mtSceneResourceConfig.AddElement(strSeedID, pInfo);
		}

		return true;
	}

	public  boolean RemoveSeedObject( String strSeedID)
	{
		return true;
	}
	
	public  boolean AddReliveInfo( int nIndex,  NFVector3 vPos)
	{
		return mtSceneRelivePos.AddElement(nIndex, new NFVector3(vPos));
	}
	public NFVector3 GetReliveInfo( int nIndex)
	{
		NFVector3 vPos = mtSceneRelivePos.GetElement(nIndex);
		if (vPos != null)
		{
			return vPos;
		}

		return null;
	}
	
	public int mnGroupIndex;
    public int mnSceneID;
    public int mnWidth;
	//seedID, seedInfo
	public NFMapEx<String, SceneSeedResource > mtSceneResourceConfig=new NFMapEx<String, SceneSeedResource>() {
	};
	public NFMapEx<Integer, NFVector3 > mtSceneRelivePos=new NFMapEx<Integer, NFVector3>() {
	};
}
