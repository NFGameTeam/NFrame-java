package com.noahframe.nfcore.iface.math;

import com.noahframe.api.utils.CRC_MODBUS16;
import com.noahframe.nfcore.iface.NFrame;

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
