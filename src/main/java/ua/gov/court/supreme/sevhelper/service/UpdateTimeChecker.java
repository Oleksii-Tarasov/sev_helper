package ua.gov.court.supreme.sevhelper.service;

public class UpdateTimeChecker {
    private static long LAST_UPDATE_TIME = 0;
    private static final long UPDATE_TIME_INTERVAL = 30 * 60 * 1000;

    public boolean canUpdate() {
        return System.currentTimeMillis() - LAST_UPDATE_TIME >= UPDATE_TIME_INTERVAL;
    }

    public void updateLastUpdateTime() {
        LAST_UPDATE_TIME = System.currentTimeMillis();
    }
}
