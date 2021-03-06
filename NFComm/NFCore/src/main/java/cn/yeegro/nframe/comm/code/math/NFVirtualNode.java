package cn.yeegro.nframe.comm.code.math;


/**
 * @Author:zoocee
 * @Date:2018/10/19 11:02
 */
public class NFVirtualNode<T> extends NFIVirtualNode {

    public NFVirtualNode(T tData, int nVirID )
    {
        super(nVirID);
        mxData = tData;
    }

    public String GetDataStr()
    {
        return (String)(mxData);
    }

    public boolean erase(int nHash)
    {
        int hash = m_pHasher.GetHashValue(this);
        return hash==nHash;
    }


    T mxData;
}
