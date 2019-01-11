package xlo.ipBlockedNotification.service;

import xlo.ipBlockedNotification.model.IpInfo;

import java.io.IOException;
import java.util.Objects;

public class HealthCheckService {

    private final ResultService resultService;

    public HealthCheckService(ResultService resultService) {
        this.resultService = resultService;
    }


    public void checkHealth(IpInfo ipInfo) {
        this.resultService.handleHealthCheck(ipInfo, checkReachable(ipInfo), checkConnectable(ipInfo));
    }

    private Result checkConnectable(IpInfo ipInfo) {
        boolean ok;
        Exception exception = null;
        try {
            ok = ipInfo.isConnectable();
        } catch (IOException e) {
            ok = false;
            exception = e;
        }
        return new Result(ok, exception);
    }

    private Result checkReachable(IpInfo ipInfo) {
        boolean ok;
        Exception exception = null;
        try {
            ok = ipInfo.isReachable();
        } catch (IOException e) {
            ok = false;
            exception = e;
        }
        return new Result(ok, exception);
    }

    public static class Result {
        private final boolean ok;
        private final Exception exception;

        public Result(boolean ok, Exception exception) {
            this.ok = ok;
            this.exception = exception;
        }

        public boolean isOk() {
            return ok;
        }

        public Exception getException() {
            return exception;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;
            return ok == result.ok &&
                    Objects.equals(exception, result.exception);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ok, exception);
        }
    }

}
