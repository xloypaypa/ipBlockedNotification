package xlo.ipBlockedNotification.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class IpInfo {

    private final String host;
    private final int port;
    private final int timeout;

    public IpInfo(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public boolean isReachable() throws IOException {
        return InetAddress.getByName(this.host).isReachable(this.timeout);
    }

    public boolean isConnectable() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.host, this.port), this.timeout);
        socket.close();
        return true;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpInfo ipInfo = (IpInfo) o;
        return port == ipInfo.port &&
                timeout == ipInfo.timeout &&
                host.equals(ipInfo.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, timeout);
    }
}
