package cn.yeegro.nframe.comm.code.math;

import java.util.*;

/**
 * @Author:zoocee
 * @Date:2018/10/19 10:56
 */
public abstract class NFConsistentHashMapEx<T,TD> implements Map<T, TD> {


    private NFConsistentHash<T> mxConsistentHash;

    public TD  GetElementBySuitRandom()
    {
        if (mxConsistentHash.GetSuitNodeRandom()!=null)
        {
            int n= new Random(this.size()).nextInt();
            Iterator it = this.entrySet().iterator();
            for (int i=0;i<=n;i++)
            {
                Entry pairs = (Entry)it.next();
                if (i==n)
                {
                    return (TD) pairs.getValue();
                }
            }
        }
        return null;
    }

    public TD GetElementBySuitConsistent()
    {
        NFVirtualNode<T> vNode=mxConsistentHash.GetSuitNodeConsistent();
        if (vNode!=null)
        {
            TD itr = this.get(vNode.mxData);
            if (itr!=null)
            {
                return itr;
            }
        }
        return null;
    }

    public TD GetElementBySuit(T name)
    {
        NFVirtualNode<T> vNode=mxConsistentHash.GetSuitNode(name);
        if (vNode!=null)
        {
            TD itr = this.get(vNode.mxData);
            if (itr!=null)
            {
                return itr;
            }
        }

        return null;
    }

    public  boolean AddElement( T name,  TD data)
    {
        if (data == null)
        {
            return false;
        }
        TD itr = this.get(name);
        if (itr ==null)
        {
            this.put(name,data);
            mxConsistentHash.Insert(name);
            return true;
        }
        return false;
    }

    public boolean RemoveElement( T name)
    {
        TD itr = this.get(name);
        if (itr != null)
        {
            this.remove(name);
            mxConsistentHash.Erase(name);
            return true;
        }
        return false;
    }

    public boolean ClearAll()
    {
        this.clear();
        mxConsistentHash.ClearAll();
        return true;
    }

}
