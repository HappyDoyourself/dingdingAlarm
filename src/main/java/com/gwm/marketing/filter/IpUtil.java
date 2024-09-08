package com.gwm.marketing.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取本地ip
 *
 * @author
 */
public class IpUtil {

    private static String localIp;

    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

    public static String initIp() {
        if (localIp == null) {
            try {
                Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                while (allNetInterfaces.hasMoreElements()) {
                    NetworkInterface netInterface = allNetInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress ip = addresses.nextElement();
                        if (ip != null
                                && ip instanceof Inet4Address
                                && !ip.isLoopbackAddress()
                                && ip.getHostAddress().indexOf(":") == -1) {
                            localIp = ip.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                logger.error(e + "");
            } catch (Exception e) {
                logger.error(e + "");
            }
        }
        return localIp;
    }

}
