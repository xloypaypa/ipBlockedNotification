package xlo.ipBlockedNotification.service;

import org.junit.Before;
import org.junit.Test;
import xlo.ipBlockedNotification.model.IpInfo;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class HealthCheckServiceTest {
    
    private IpInfoService ipInfoService;
    private HealthCheckService healthCheckService;

    @Before
    public void setUp() {
        this.ipInfoService = mock(IpInfoService.class);
        this.healthCheckService = new HealthCheckService(ipInfoService);
    }

    @Test
    public void should_handle_ping_and_connect_success() throws Exception {
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenReturn(true);
        when(ipInfo.isConnectable()).thenReturn(true);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.ipInfoService).handlePingSuccess(ipInfo);
        verify(this.ipInfoService).handleConnectSuccess(ipInfo);
    }

    @Test
    public void should_handle_ping_success_but_connect_failed() throws Exception {
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenReturn(true);
        when(ipInfo.isConnectable()).thenReturn(false);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.ipInfoService).handlePingSuccess(ipInfo);
        verify(this.ipInfoService).handleConnectFailed(ipInfo, null);
    }

    @Test
    public void should_handle_ping_success_but_connect_exception() throws Exception {
        IOException ioException = new IOException();
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenReturn(true);
        when(ipInfo.isConnectable()).thenThrow(ioException);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.ipInfoService).handlePingSuccess(ipInfo);
        verify(this.ipInfoService).handleConnectFailed(ipInfo, ioException);
    }

    @Test
    public void should_handle_ping_fail_but_connect_success() throws Exception {
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenReturn(false);
        when(ipInfo.isConnectable()).thenReturn(true);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.ipInfoService).handlePingFailed(ipInfo, null);
        verify(this.ipInfoService).handleConnectSuccess(ipInfo);
    }

    @Test
    public void should_handle_ping_exception_but_connect_success() throws Exception {
        IOException ioException = new IOException();
        IpInfo ipInfo = mock(IpInfo.class);
        when(ipInfo.isReachable()).thenThrow(ioException);
        when(ipInfo.isConnectable()).thenReturn(true);

        this.healthCheckService.checkHealth(ipInfo);

        verify(this.ipInfoService).handlePingFailed(ipInfo, ioException);
        verify(this.ipInfoService).handleConnectSuccess(ipInfo);
    }

}