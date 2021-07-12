package cn.yeegro.nframe.comm.code.math;


import cn.yeegro.nframe.comm.code.util.CRC_MODBUS16;

/**
 * @Author:zoocee
 * @Date:2018/10/19 11:21
 */
public class NFHasher implements NFIHasher {
    @Override
    public int GetHashValue(NFIVirtualNode vNode) {
        String vnode = vNode.ToStr();
        return CRC_MODBUS16.getCRC(vnode.getBytes());
    }
}
