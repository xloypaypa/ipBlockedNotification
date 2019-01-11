package xlo.ipBlockedNotification.service;

import org.junit.Before;
import org.junit.Test;
import xlo.ipBlockedNotification.model.IpInfo;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class HealthCheckServiceTest {

    private ResultService resultService;
    private HealthCheckService healthCheckService;

    @Before
    public void setUp() {
        this.resultService = mock(ResultService.class);
        this.healthCheckService = new HealthCheckService(resultService);
    }

    @Test
    public void should_handle_ping_and_connect_success() throws Exception {
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenReturn(true);
        when(ipInfo.isConnectable()).thenReturn(true);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.resultService).handleHealthCheck(same(ipInfo), eq(new HealthCheckService.Result(true, null)),
                eq(new HealthCheckService.Result(true, null)));
    }

    @Test
    public void should_handle_ping_success_but_connect_failed() throws Exception {
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenReturn(true);
        when(ipInfo.isConnectable()).thenReturn(false);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.resultService).handleHealthCheck(same(ipInfo), eq(new HealthCheckService.Result(true, null)),
                eq(new HealthCheckService.Result(false, null)));
    }

    @Test
    public void should_handle_ping_success_but_connect_exception() throws Exception {
        IOException ioException = new IOException();
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenReturn(true);
        when(ipInfo.isConnectable()).thenThrow(ioException);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.resultService).handleHealthCheck(same(ipInfo), eq(new HealthCheckService.Result(true, null)),
                eq(new HealthCheckService.Result(false, ioException)));
    }

    @Test
    public void should_handle_ping_fail_but_connect_success() throws Exception {
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenReturn(false);
        when(ipInfo.isConnectable()).thenReturn(true);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.resultService).handleHealthCheck(same(ipInfo), eq(new HealthCheckService.Result(false, null)),
                eq(new HealthCheckService.Result(true, null)));
    }

    @Test
    public void should_handle_ping_exception_but_connect_success() throws Exception {
        IOException ioException = new IOException();
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenThrow(ioException);
        when(ipInfo.isConnectable()).thenReturn(true);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.resultService).handleHealthCheck(same(ipInfo), eq(new HealthCheckService.Result(false, ioException)),
                eq(new HealthCheckService.Result(true, null)));
    }

}