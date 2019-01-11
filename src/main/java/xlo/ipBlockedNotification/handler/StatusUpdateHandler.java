package xlo.ipBlockedNotification.handler;

import xlo.ipBlockedNotification.model.IpInfo;
import xlo.ipBlockedNotification.model.IpStatus;

import javax.mail.MessagingException;

public interface StatusUpdateHandler {

    void handleStatusUpdate(IpInfo ipInfo, IpStatus ipStatus) throws MessagingException;

}
