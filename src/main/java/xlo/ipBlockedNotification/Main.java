package xlo.ipBlockedNotification;

import xlo.ipBlockedNotification.config.NotificationConfig;
import xlo.ipBlockedNotification.handler.EmailStatusUpdateHandler;
import xlo.ipBlockedNotification.handler.StatusUpdateHandler;
import xlo.ipBlockedNotification.model.IpInfo;
import xlo.ipBlockedNotification.service.HealthCheckService;
import xlo.ipBlockedNotification.service.ResultService;
import xlo.ipBlockedNotification.service.StatusService;
import xlo.ipBlockedNotification.service.TimerService;

public class Main {

    public static void main(String[] args) {
        StatusUpdateHandler[] statusUpdateHandlers = new StatusUpdateHandler[]{
                new EmailStatusUpdateHandler(args[0], args[1], args[2], args[3])
        };

        IpInfo[] ipInfos = new IpInfo[args.length / 2 - 2];
        for (int i = 4; i < args.length; i += 2) {
            ipInfos[i / 2 - 2] = new IpInfo(args[i], Integer.parseInt(args[i + 1]), 5000);

        }

        NotificationConfig notificationConfig = new NotificationConfig(30 * 1000, 3600 * 1000, 3, 5);
        StatusService statusService = new StatusService(notificationConfig, ipInfos);
        ResultService resultService = new ResultService(statusService);
        HealthCheckService healthCheckService = new HealthCheckService(resultService);

        new TimerService(statusService, healthCheckService, statusUpdateHandlers).start();
    }
}
