package xlo.ipBlockedNotification.config;

public class NotificationConfig {

    private final long upInter, downInter;

    private final int blockedCount;
    private final int backToNormalCount;

    public NotificationConfig(long upInter, long downInter, int blockedCount, int backToNormalCount) {
        this.upInter = upInter;
        this.downInter = downInter;
        this.blockedCount = blockedCount;
        this.backToNormalCount = backToNormalCount;
    }

    public long getUpInter() {
        return upInter;
    }

    public long getDownInter() {
        return downInter;
    }

    public int getBlockedCount() {
        return blockedCount;
    }

    public int getBackToNormalCount() {
        return backToNormalCount;
    }
}
