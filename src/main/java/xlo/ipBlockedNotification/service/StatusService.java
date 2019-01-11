package xlo.ipBlockedNotification.service;

import org.joda.time.DateTime;
import xlo.ipBlockedNotification.config.NotificationConfig;
import xlo.ipBlockedNotification.model.IpInfo;
import xlo.ipBlockedNotification.model.IpStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatusService {

    private final NotificationConfig notificationConfig;
    private final Map<IpInfo, IpStatus> statusMap = new HashMap<>();

    public StatusService(NotificationConfig notificationConfig, IpInfo... ipInfos) {
        this.notificationConfig = notificationConfig;
        for (IpInfo ipInfo : ipInfos) {
            this.statusMap.put(ipInfo, new IpStatus());
        }
    }

    public IpStatus getStatus(IpInfo ipInfo) {
        synchronized (this.statusMap) {
            return this.statusMap.get(ipInfo);
        }
    }

    public void updateStatus(IpInfo ipInfo, IpStatus ipStatus) {
        synchronized (this.statusMap) {
            this.statusMap.put(ipInfo, ipStatus);
        }
    }

    public Set<IpInfo> needDoCheck(DateTime date) {
        synchronized (this.statusMap) {
            Set<IpInfo> result = new HashSet<>();
            for (Map.Entry<IpInfo, IpStatus> now : this.statusMap.entrySet()) {
                IpStatus ipStatus = now.getValue();
                boolean allUp = isUpOrUnknown(ipStatus.getPingStatus()) && isUpOrUnknown(ipStatus.getConnectStatus());
                DateTime lastCheckDate = ipStatus.getLastCheckDate();
                if (allUp && date.getMillis() - lastCheckDate.getMillis() >= notificationConfig.getUpInter()) {
                    result.add(now.getKey());
                } else if (!allUp && date.getMillis() - lastCheckDate.getMillis() >= notificationConfig.getDownInter()) {
                    result.add(now.getKey());
                }
            }
            return result;
        }
    }

    private boolean isUpOrUnknown(IpStatus.CountStatus countStatus) {
        return countStatus.getStatus().equals(IpStatus.Status.UP) || countStatus.getStatus().equals(IpStatus.Status.UNKNOWN);
    }

    public Map<IpInfo, IpStatus> needSendNotification() {
        synchronized (this.statusMap) {
            Map<IpInfo, IpStatus> result = new HashMap<>();
            for (Map.Entry<IpInfo, IpStatus> now : this.statusMap.entrySet()) {
                IpStatus ipStatus = now.getValue();
                if (needSendNotification(ipStatus.getConnectStatus()) || needSendNotification(ipStatus.getPingStatus())) {
                    result.put(now.getKey(), now.getValue());
                }
            }
            return result;
        }
    }

    private boolean needSendNotification(IpStatus.CountStatus countStatus) {
        if (countStatus.getStatus().equals(IpStatus.Status.UP) && countStatus.getCurrentStatusCount() == notificationConfig.getBackToNormalCount()) {
            return true;
        } else {
            return countStatus.getStatus().equals(IpStatus.Status.DOWN) && countStatus.getCurrentStatusCount() == notificationConfig.getBlockedCount();
        }
    }

}
