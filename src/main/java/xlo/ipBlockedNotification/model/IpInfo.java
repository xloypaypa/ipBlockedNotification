package xlo.ipBlockedNotification.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class IpInfo {

    private final String host;
    private final int port;
    private final int timeout;

    public IpInfo(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.pingStatus = new CountStatus();
        this.connectStatus = new CountStatus();
    }

    private CountStatus pingStatus, connectStatus;

    public boolean isReachable() throws IOException {
        return InetAddress.getByName(this.host).isReachable(this.timeout);
    }

    public boolean isConnectable() throws IOException {
        new Socket().connect(new InetSocketAddress(this.host, this.port), this.timeout);
        return true;
    }

    public static class CountStatus {
        private Status status;
        private int previousStatusCount, currentStatusCount;

        public CountStatus() {
            this.status = Status.UNKNOWN;
            this.previousStatusCount = 0;
            this.currentStatusCount = 0;
        }

        public CountStatus(Status status, int previousStatusCount, int currentStatusCount) {
            this.status = status;
            this.previousStatusCount = previousStatusCount;
            this.currentStatusCount = currentStatusCount;
        }

        public void updateStatus(Status status) {
            if (status.equals(this.status)) {
                this.currentStatusCount++;
            } else {
                this.status = status;
                this.previousStatusCount = this.currentStatusCount;
                this.currentStatusCount = 1;
            }
        }

        public Status getStatus() {
            return status;
        }

        public int getPreviousStatusCount() {
            return previousStatusCount;
        }

        public int getCurrentStatusCount() {
            return currentStatusCount;
        }
    }

    public enum Status {
        UP, DOWN, UNKNOWN
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

    public CountStatus getPingStatus() {
        return pingStatus;
    }

    public CountStatus getConnectStatus() {
        return connectStatus;
    }
}
