package cn.yeegro.nframe.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IPUtils {
	
	/**
     * 获取本机内网IP地址方法
     * @return
     */
   public static String getHostIp(){
        try{
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && ip.getHostAddress().indexOf(":")==-1){
                        return ip.getHostAddress();
                    }
                }
            }
        }catch(Exception e){
            log.error(e.getStackTrace()[0].getLineNumber() + e.getStackTrace()[0].getClassName());
        }
        return null;
    }
}
