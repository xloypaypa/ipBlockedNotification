package xlo.ipBlockedNotification.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xlo.ipBlockedNotification.config.NotificationConfig;
import xlo.ipBlockedNotification.model.IpInfo;
import xlo.ipBlockedNotification.model.IpStatus;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatusServiceTest {

    private IpInfo ipInfo1, ipInfo2;
    private StatusService statusService;

    private IpStatus.CountStatus matchBackToNormal, notMatchBackToNormal, matchBlock, notMatchBlock;

    @Before
    public void setUp() {
        this.ipInfo1 = mock(IpInfo.class);
        this.ipInfo2 = mock(IpInfo.class);
        NotificationConfig notificationConfig = mock(NotificationConfig.class);

        when(notificationConfig.getUpInter()).thenReturn(30000L);
        when(notificationConfig.getDownInter()).thenReturn(3600000L);
        when(notificationConfig.getBackToNormalCount()).thenReturn(5);
        when(notificationConfig.getBlockedCount()).thenReturn(3);

        this.statusService = new StatusService(notificationConfig, this.ipInfo1, this.ipInfo2);

        this.matchBackToNormal = new IpStatus.CountStatus(IpStatus.Status.UP, 100, 5);
        this.notMatchBackToNormal = new IpStatus.CountStatus(IpStatus.Status.UP, 100, 3);

        this.matchBlock = new IpStatus.CountStatus(IpStatus.Status.DOWN, 100, 3);
        this.notMatchBlock = new IpStatus.CountStatus(IpStatus.Status.DOWN, 100, 2);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void should_send_when_one_of_status_match_config() {
        IpStatus ipStatus = mock(IpStatus.class);
        when(ipStatus.getConnectStatus()).thenReturn(this.matchBackToNormal);
        when(ipStatus.getPingStatus()).thenReturn(this.notMatchBlock);

        this.statusService.updateStatus(this.ipInfo1, ipStatus);

        assertNotNull(this.statusService.needSendNotification().get(this.ipInfo1));
        assertNull(this.statusService.needSendNotification().get(this.ipInfo2));
    }

    @Test
    public void should_send_all_ip_if_more_than_one_ip_match_config() {
        IpStatus ipStatus = mock(IpStatus.class);
        when(ipStatus.getConnectStatus()).thenReturn(this.matchBackToNormal);
        when(ipStatus.getPingStatus()).thenReturn(this.notMatchBlock);

        this.statusService.updateStatus(this.ipInfo1, ipStatus);
        this.statusService.updateStatus(this.ipInfo2, ipStatus);

        assertNotNull(this.statusService.needSendNotification().get(this.ipInfo1));
        assertNotNull(this.statusService.needSendNotification().get(this.ipInfo2));
    }

    @Test
    public void should_get_need_check_ip() {
        DateTimeUtils.setCurrentMillisFixed(1000);
        IpStatus ipStatus1 = new IpStatus(this.matchBackToNormal, this.notMatchBackToNormal);
        IpStatus ipStatus2 = new IpStatus(this.notMatchBackToNormal, this.notMatchBlock);
        this.statusService.updateStatus(this.ipInfo1, ipStatus1);
        this.statusService.updateStatus(this.ipInfo2, ipStatus2);

        Set<IpInfo> result = this.statusService.needDoCheck(new DateTime(1000 + 3000000));

        assertTrue(result.contains(this.ipInfo1));
        assertFalse(result.contains(this.ipInfo2));
    }
}