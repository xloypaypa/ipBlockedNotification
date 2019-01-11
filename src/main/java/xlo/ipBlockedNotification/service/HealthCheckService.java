package xlo.ipBlockedNotification.service;

import xlo.ipBlockedNotification.model.IpInfo;

import java.io.IOException;

public class HealthCheckService {

    private final IpInfoService ipInfoService;

    public HealthCheckService(IpInfoService ipInfoService) {
        this.ipInfoService = ipInfoService;
    }


    public void checkHealth(IpInfo ipInfo) {
        checkReachable(ipInfo);
        checkConnectable(ipInfo);
    }

    private void checkConnectable(IpInfo ipInfo) {
        try {
            if (ipInfo.isConnectable()) {
                ipInfoService.handleConnectSuccess(ipInfo);
            } else {
                ipInfoService.handleConnectFailed(ipInfo, null);
            }

        } catch (IOException e) {
            ipInfoService.handleConnectFailed(ipInfo, e);
        }
    }

    private void checkReachable(IpInfo ipInfo) {
        try {
            if (ipInfo.isReachable()) {
                ipInfoService.handlePingSuccess(ipInfo);
            } else {
                ipInfoService.handlePingFailed(ipInfo, null);
            }
        } catch (IOException e) {
            ipInfoService.handlePingFailed(ipInfo, e);
        }
    }

}
