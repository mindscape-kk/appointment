package co.mscp;

import co.mscp.logging.Logger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtil {

    public static InetAddress getHostInet() {
        Enumeration<NetworkInterface> n = null;
        try {
            n = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new MonitoredError(e);
        }
        for (; n.hasMoreElements();) {
            NetworkInterface e = n.nextElement();
            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();) {
                InetAddress addr = a.nextElement();
                if(!addr.isLoopbackAddress() && !addr.isLinkLocalAddress()) {
                    return addr;
                }
            }
        }

        throw new MonitoredError("Local host IP could not be resolved");
    }

}
