package com.noahframe.nfcore.iface.math;

import com.noahframe.api.utils.CRC_MODBUS16;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author:zoocee
 * @Date:2018/10/19 11:32
 */
public class NFConsistentHash<T> implements NFIConsistentHash<T> {

    public NFConsistentHash()
    {
        m_pHasher = new NFHasher();
        mxNodes=new HashMap<>();
    }

    @Override
    public int Size() {
        return mxNodes.size();
    }

    @Override
    public boolean Empty() {
        return mxNodes.isEmpty();
    }

    @Override
    public void ClearAll() {
        mxNodes.clear();
    }

    @Override
    public void Insert(T name) {
        for (int i = 0; i < mnNodeCount; ++i)
        {
            NFVirtualNode<T> vNode=new NFVirtualNode<>(name,i);
            Insert(vNode);
        }
    }

    @Override
    public void Insert(NFVirtualNode<T> xNode) {

        int hash = m_pHasher.GetHashValue(xNode);
        NFVirtualNode<T> it = mxNodes.get(hash);
        if (it == null)
        {
            mxNodes.put(hash,xNode);
        }
    }

    @Override
    public boolean Exist(NFVirtualNode<T> xInNode) {

        int hash = m_pHasher.GetHashValue(xInNode);
        NFVirtualNode<T>  it = mxNodes.get(hash);
        if (it != null)
        {
            return true;
        }
        return false;
    }

    @Override
    public void Erase(T name) {
        for (int i = 0; i < mnNodeCount; ++i)
        {
            NFVirtualNode<T> vNode=new NFVirtualNode<>(name,i);
            Erase(vNode);
        }
    }

    @Override
    public int Erase(NFVirtualNode<T> xNode) {
        int hash = m_pHasher.GetHashValue(xNode);
        int nCount=0;
        for (Map.Entry<Integer, NFVirtualNode<T>> entry : mxNodes.entrySet()) {
            if (entry.getValue().erase(hash)){
                nCount++;
            }
        }
        return nCount;
    }

    @Override
    public NFVirtualNode<T> GetSuitNodeRandom() {
        int nID = (int)System.currentTimeMillis()/(1000*60*60*24);
        return GetSuitNode(nID);
    }

    @Override
    public NFVirtualNode<T> GetSuitNodeConsistent() {

        return GetSuitNode(0);
    }

    @Override
    public NFVirtualNode<T> GetSuitNode(T name) {
        String str = String.valueOf(name);
        int nCRC32 = CRC_MODBUS16.getCRC(str.getBytes());
        return GetSuitNode(nCRC32);
    }

    @Override
    public NFVirtualNode<T> GetSuitNode(int hashValue) {
        if(mxNodes.values().size()==0)
        {
            return null;
        }
        return mxNodes.get(hashValue);

    }

    @Override
    public boolean GetNodeList(List<NFVirtualNode<T>> nfVirtualNodes) {
        return false;
    }


    private int mnNodeCount = 500;
    private Map<Integer, NFVirtualNode<T>> mxNodes;
    private NFIHasher m_pHasher;
}
