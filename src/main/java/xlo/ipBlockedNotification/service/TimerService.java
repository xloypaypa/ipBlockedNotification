package xlo.ipBlockedNotification.service;

import org.joda.time.DateTime;
import xlo.ipBlockedNotification.handler.StatusUpdateHandler;
import xlo.ipBlockedNotification.model.IpInfo;
import xlo.ipBlockedNotification.model.IpStatus;

import javax.mail.MessagingException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TimerService {

    private final StatusService statusService;
    private final HealthCheckService healthCheckService;
    private final StatusUpdateHandler[] statusUpdateHandlers;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public TimerService(StatusService statusService, HealthCheckService healthCheckService, StatusUpdateHandler[] statusUpdateHandlers) {
        this.statusService = statusService;
        this.healthCheckService = healthCheckService;
        this.statusUpdateHandlers = statusUpdateHandlers;
    }

    public void start() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Set<IpInfo> ipInfos = statusService.needDoCheck(DateTime.now());
                    if (ipInfos.isEmpty()) return;

                    Set<Future> futures = new HashSet<>();
                    for (IpInfo ipInfo : ipInfos) {
                        futures.add(executor.submit(() -> healthCheckService.checkHealth(ipInfo)));
                    }

                    for (Future future : futures) {
                        future.get();
                    }

                    Map<IpInfo, IpStatus> result = statusService.needSendNotification();
                    for (Map.Entry<IpInfo, IpStatus> now : result.entrySet()) {
                        if (!ipInfos.contains(now.getKey())) continue;
                        for (StatusUpdateHandler statusUpdateHandler : statusUpdateHandlers) {
                            try {
                                statusUpdateHandler.handleStatusUpdate(now.getKey(), now.getValue());
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 500, 500);
    }

}
