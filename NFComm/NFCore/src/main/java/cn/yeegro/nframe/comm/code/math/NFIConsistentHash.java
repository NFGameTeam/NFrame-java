package cn.yeegro.nframe.comm.code.math;

import java.util.List;

/**
 * @Author:zoocee
 * @Date:2018/10/19 11:33
 */
public interface NFIConsistentHash<T> {

    public int Size()  ;
    public boolean Empty()  ;

    public void ClearAll() ;
    public void Insert(T name) ;
    public void Insert(NFVirtualNode<T> xNode) ;

    public boolean Exist(NFVirtualNode<T> xInNode) ;
    public void Erase(T name) ;
    public int Erase(NFVirtualNode<T> xNode)  ;

    public NFVirtualNode<T> GetSuitNodeRandom() ;
    public NFVirtualNode<T> GetSuitNodeConsistent() ;
    public NFVirtualNode<T> GetSuitNode(T name) ;
    //public boolean GetSuitNode( std::string str, NFVirtualNode<T> node) ;
    public NFVirtualNode<T> GetSuitNode(int hashValue) ;

    public boolean GetNodeList(List<NFVirtualNode<T>> nodeList) ;

}
