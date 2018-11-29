package com.noahframe.nfcore.iface.module;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author:zoocee
 * @Date:2018/11/15 14:37
 */
public interface NFIRedisCluster<T>{

 public boolean AddNode(T redis);

 public  boolean Connecting();

 public boolean isConnecting();

}
