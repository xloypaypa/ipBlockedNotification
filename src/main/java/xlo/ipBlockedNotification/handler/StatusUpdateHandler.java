package xlo.ipBlockedNotification.handler;

import xlo.ipBlockedNotification.model.IpInfo;

import javax.mail.MessagingException;

public interface StatusUpdateHandler {

    void handleStatusUpdate(IpInfo ipInfo) throws MessagingException;

}
