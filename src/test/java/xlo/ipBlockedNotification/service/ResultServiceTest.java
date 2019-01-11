package xlo.ipBlockedNotification.service;

import org.junit.Before;
import org.junit.Test;
import xlo.ipBlockedNotification.model.IpInfo;
import xlo.ipBlockedNotification.model.IpStatus;

import static org.mockito.Mockito.*;

public class ResultServiceTest {

    private IpStatus.CountStatus ping, connect;
    private IpInfo ipInfo;
    private StatusService statusService;
    private ResultService resultService;

    @Before
    public void setUp() {
        this.statusService = mock(StatusService.class);
        IpStatus ipStatus = mock(IpStatus.class);
        this.ipInfo = mock(IpInfo.class);
        this.ping = mock(IpStatus.CountStatus.class);
        this.connect = mock(IpStatus.CountStatus.class);

        when(ipStatus.getPingStatus()).thenReturn(this.ping);
        when(ipStatus.getConnectStatus()).thenReturn(this.connect);
        when(this.statusService.getStatus(this.ipInfo)).thenReturn(ipStatus);

        this.resultService = new ResultService(this.statusService);
    }

    @Test
    public void should_plus_one_for_current_when_result_is_same_with_before() {
        when(this.ping.getStatus()).thenReturn(IpStatus.Status.UP);
        when(this.ping.getCurrentStatusCount()).thenReturn(100);
        when(this.ping.getPreviousStatusCount()).thenReturn(200);
        when(this.connect.getStatus()).thenReturn(IpStatus.Status.DOWN);
        when(this.connect.getCurrentStatusCount()).thenReturn(300);
        when(this.connect.getPreviousStatusCount()).thenReturn(400);

        this.resultService.handleHealthCheck(this.ipInfo, new HealthCheckService.Result(true, null),
                new HealthCheckService.Result(false, null));


        verify(this.statusService).updateStatus(same(this.ipInfo),
                eq(new IpStatus(new IpStatus.CountStatus(IpStatus.Status.UP, 200, 101),
                        new IpStatus.CountStatus(IpStatus.Status.DOWN, 400, 301))));
    }

    @Test
    public void should_move_current_to_previous_when_result_is_different_with_before() {
        when(this.ping.getStatus()).thenReturn(IpStatus.Status.DOWN);
        when(this.ping.getCurrentStatusCount()).thenReturn(100);
        when(this.ping.getPreviousStatusCount()).thenReturn(200);
        when(this.connect.getStatus()).thenReturn(IpStatus.Status.UP);
        when(this.connect.getCurrentStatusCount()).thenReturn(300);
        when(this.connect.getPreviousStatusCount()).thenReturn(400);

        this.resultService.handleHealthCheck(this.ipInfo, new HealthCheckService.Result(true, null),
                new HealthCheckService.Result(false, null));


        verify(this.statusService).updateStatus(same(this.ipInfo),
                eq(new IpStatus(new IpStatus.CountStatus(IpStatus.Status.UP, 100, 1),
                        new IpStatus.CountStatus(IpStatus.Status.DOWN, 300, 1))));
    }

    @Test
    public void should_ping_connect_different() {
        when(this.ping.getStatus()).thenReturn(IpStatus.Status.UP);
        when(this.ping.getCurrentStatusCount()).thenReturn(100);
        when(this.ping.getPreviousStatusCount()).thenReturn(200);
        when(this.connect.getStatus()).thenReturn(IpStatus.Status.UP);
        when(this.connect.getCurrentStatusCount()).thenReturn(300);
        when(this.connect.getPreviousStatusCount()).thenReturn(400);

        this.resultService.handleHealthCheck(this.ipInfo, new HealthCheckService.Result(true, null),
                new HealthCheckService.Result(false, null));


        verify(this.statusService).updateStatus(same(this.ipInfo),
                eq(new IpStatus(new IpStatus.CountStatus(IpStatus.Status.UP, 200, 101),
                        new IpStatus.CountStatus(IpStatus.Status.DOWN, 300, 1))));
    }
}