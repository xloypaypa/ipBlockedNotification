package xlo.ipBlockedNotification.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class IpInfoTest {

    @Test
    public void should_add_one_to_current_status_when_status_not_change() {
        IpInfo.CountStatus countStatus = new IpInfo.CountStatus(IpInfo.Status.UP, 1, 100);

        countStatus.updateStatus(IpInfo.Status.UP);

        assertEquals(1, countStatus.getPreviousStatusCount());
        assertEquals(101, countStatus.getCurrentStatusCount());
    }

    @Test
    public void should_move_to_previous_and_renew_current_when_status_change() {
        IpInfo.CountStatus countStatus = new IpInfo.CountStatus(IpInfo.Status.UP, 1, 100);

        countStatus.updateStatus(IpInfo.Status.DOWN);

        assertEquals(100, countStatus.getPreviousStatusCount());
        assertEquals(1, countStatus.getCurrentStatusCount());
    }
}