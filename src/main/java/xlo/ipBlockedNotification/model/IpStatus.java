package xlo.ipBlockedNotification.model;

import org.joda.time.DateTime;

import java.util.Objects;

public class IpStatus {

    private final CountStatus pingStatus, connectStatus;
    private final DateTime lastCheckDate;

    public IpStatus() {
        this.pingStatus = new CountStatus(Status.UNKNOWN, 0, 0);
        this.connectStatus = new CountStatus(Status.UNKNOWN, 0, 0);
        this.lastCheckDate = DateTime.now();
    }

    public IpStatus(CountStatus pingStatus, CountStatus connectStatus) {
        this.pingStatus = pingStatus;
        this.connectStatus = connectStatus;
        this.lastCheckDate = DateTime.now();
    }

    public CountStatus getPingStatus() {
        return pingStatus;
    }

    public CountStatus getConnectStatus() {
        return connectStatus;
    }

    public DateTime getLastCheckDate() {
        return lastCheckDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpStatus ipStatus = (IpStatus) o;
        return Objects.equals(pingStatus, ipStatus.pingStatus) &&
                Objects.equals(connectStatus, ipStatus.connectStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pingStatus, connectStatus);
    }

    public static class CountStatus {
        private final Status status;
        private final int previousStatusCount, currentStatusCount;

        public CountStatus(Status status, int previousStatusCount, int currentStatusCount) {
            this.status = status;
            this.previousStatusCount = previousStatusCount;
            this.currentStatusCount = currentStatusCount;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CountStatus that = (CountStatus) o;
            return previousStatusCount == that.previousStatusCount &&
                    currentStatusCount == that.currentStatusCount &&
                    status == that.status;
        }

        @Override
        public int hashCode() {
            return Objects.hash(status, previousStatusCount, currentStatusCount);
        }
    }

    public enum Status {
        UP, DOWN, UNKNOWN
    }
}
