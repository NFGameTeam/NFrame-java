package com.noahframe.nfcore.iface.math;

/**
 * @Author:zoocee
 * @Date:2018/10/19 11:07
 */
public abstract class NFIVirtualNode {

    public NFIVirtualNode(int nVirID)
    {
        m_pHasher = new NFHasher();
        nVirtualIndex=nVirID;
    }

    public NFIVirtualNode()
    {
        nVirtualIndex = 0;
    }


    public String GetDataStr()
    {
        return "";
    }

    public String ToStr()
    {
        String strInfo;
        strInfo = GetDataStr() + "-" + nVirtualIndex;
        return strInfo;
    }

    private int nVirtualIndex;
    protected NFIHasher m_pHasher;

}
