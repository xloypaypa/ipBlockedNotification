package xlo.ipBlockedNotification.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class IpInfo {

    private final String host;
    private final int port;
    private final int timeout;

    public IpInfo(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.pingStatus = Status.UNKNOWN;
        this.connectStatus = Status.UNKNOWN;
        this.successfulTimes = 0;
        this.failedTimes = 0;
    }

    private Status pingStatus, connectStatus;
    private int successfulTimes, failedTimes;

    public boolean isReachable() throws IOException {
        return InetAddress.getByName(this.host).isReachable(this.timeout);
    }

    public boolean isConnectable() throws IOException {
        new Socket().connect(new InetSocketAddress(this.host, this.port), this.timeout);
        return true;
    }

    public enum Status {
        UP, DOWN, UNKNOWN
    }

}
