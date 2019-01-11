package xlo.ipBlockedNotification.service;

import xlo.ipBlockedNotification.model.IpInfo;
import xlo.ipBlockedNotification.model.IpStatus;

public class ResultService {

    private final StatusService statusService;

    public ResultService(StatusService statusService) {
        this.statusService = statusService;
    }

    public void handleHealthCheck(IpInfo ipInfo, HealthCheckService.Result pingResult, HealthCheckService.Result connectResult) {
        System.out.println(ipInfo.getHost() + " " + pingResult.isOk() + " " + connectResult.isOk());

        IpStatus previousStatus = this.statusService.getStatus(ipInfo);
        IpStatus.Status nextPingStatus = pingResult.isOk() ? IpStatus.Status.UP : IpStatus.Status.DOWN;
        IpStatus.Status nextConnectStatus = connectResult.isOk() ? IpStatus.Status.UP : IpStatus.Status.DOWN;

        IpStatus.CountStatus nextPing = getNextCountStatus(previousStatus.getPingStatus(), nextPingStatus);
        IpStatus.CountStatus nextCount = getNextCountStatus(previousStatus.getConnectStatus(), nextConnectStatus);

        IpStatus nextStatus = new IpStatus(nextPing, nextCount);
        this.statusService.updateStatus(ipInfo, nextStatus);
    }

    private IpStatus.CountStatus getNextCountStatus(IpStatus.CountStatus previousCountStatus, IpStatus.Status nextStatus) {
        IpStatus.CountStatus nextPing;
        if (previousCountStatus.getStatus().equals(nextStatus)) {
            nextPing = new IpStatus.CountStatus(nextStatus, previousCountStatus.getPreviousStatusCount(),
                    previousCountStatus.getCurrentStatusCount() + 1);
        } else {
            nextPing = new IpStatus.CountStatus(nextStatus, previousCountStatus.getCurrentStatusCount(), 1);
        }
        return nextPing;
    }

}
