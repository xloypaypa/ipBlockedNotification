package xlo.ipBlockedNotification.handler;

import xlo.ipBlockedNotification.model.IpInfo;

public interface BlockHandler {

    void handleBlock(IpInfo ipInfo);

}
